package com.example.myswap

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.myswap.data.AppDatabase
import com.example.myswap.data.SwapHistory
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.util.*

class SwapFragment : Fragment() {

    private lateinit var etFromAmount: EditText
    private lateinit var etToAmount: EditText
    private lateinit var spinnerFromCurrency: Spinner
    private lateinit var spinnerToCurrency: Spinner
    private lateinit var tvRateFrom: TextView
    private lateinit var tvRateTo: TextView
    private lateinit var btnCreateSwap: Button

    private val currencies = listOf(
        "USD", "EUR", "JPY", "SGD", "AUD", "GBP",
        "IDR", "MYR", "THB", "VND", "PHP", "LAK",
        "INR", "SAR", "RUB", "CNY"
    )

    private var debounceJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_swap, container, false)

        // Binding UI
        etFromAmount = view.findViewById(R.id.etFromAmount)
        etToAmount = view.findViewById(R.id.etToAmount)
        spinnerFromCurrency = view.findViewById(R.id.spinnerFromCurrency)
        spinnerToCurrency = view.findViewById(R.id.spinnerToCurrency)
        tvRateFrom = view.findViewById(R.id.tvRateFrom)
        tvRateTo = view.findViewById(R.id.tvRateTo)
        btnCreateSwap = view.findViewById(R.id.btnCreateSwap)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromCurrency.adapter = adapter
        spinnerToCurrency.adapter = adapter

        // Default selection
        spinnerFromCurrency.setSelection(currencies.indexOf("USD"))
        spinnerToCurrency.setSelection(currencies.indexOf("IDR"))

        // TextWatcher dengan debounce (tidak spam API saat mengetik cepat)
        etFromAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                debounceTrigger()
            }
        })

        // Spinner selection listener
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                debounceTrigger()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerFromCurrency.onItemSelectedListener = spinnerListener
        spinnerToCurrency.onItemSelectedListener = spinnerListener

        // Optional manual button
        btnCreateSwap.setOnClickListener {
            val fromCurrency = spinnerFromCurrency.selectedItem.toString()
            val toCurrency = spinnerToCurrency.selectedItem.toString()

            val fromAmount = etFromAmount.text.toString().toDoubleOrNull() ?: 0.0
            val toAmount = etToAmount.text.toString().replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0

            if (fromAmount == 0.0 || toAmount == 0.0) {
                Toast.makeText(requireContext(), "Masukkan nominal yang valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = AppDatabase.getDatabase(requireContext())
            val history = SwapHistory(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                fromAmount = fromAmount,
                toAmount = toAmount
            )

            CoroutineScope(Dispatchers.IO).launch {
                db.swapHistoryDao().insert(history)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Swap berhasil disimpan!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        return view
    }

    private fun debounceTrigger() {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(Dispatchers.Main).launch {
            delay(500) // tunggu 500ms setelah terakhir user mengetik/pilih
            triggerExchange()
        }
    }

    private fun triggerExchange() {
        val fromCurrency = spinnerFromCurrency.selectedItem.toString()
        val toCurrency = spinnerToCurrency.selectedItem.toString()
        val amountText = etFromAmount.text.toString()

        val amount = amountText.toDoubleOrNull()
        if (amount != null) {
            getExchangeRate(fromCurrency, toCurrency, amount)
        } else {
            etToAmount.setText("")
            tvRateFrom.text = ""
            tvRateTo.text = ""
        }
    }

    private fun getExchangeRate(from: String, to: String, amount: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiUrl = "https://api.frankfurter.app/latest?amount=$amount&from=$from&to=$to"
                val response = URL(apiUrl).readText()
                val jsonObject = JSONObject(response)
                val result = jsonObject.getJSONObject("rates").getDouble(to)

                withContext(Dispatchers.Main) {
                    // Format angka ke dalam locale Indonesia (misalnya "10.000,50")
                    val localeID = Locale("in", "ID")
                    val formatter = NumberFormat.getNumberInstance(localeID).apply {
                        maximumFractionDigits = 2
                        minimumFractionDigits = 2
                    }

                    val formattedResult = formatter.format(result)
                    val formattedRate = formatter.format(result / amount)

                    etToAmount.setText(formattedResult)
                    tvRateFrom.text = "1 $from"
                    tvRateTo.text = "= $formattedRate $to"

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Gagal: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}