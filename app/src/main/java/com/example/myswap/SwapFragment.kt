package com.example.myswap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class SwapFragment : Fragment() {

    private lateinit var etFromAmount: EditText
    private lateinit var etToAmount: EditText
    private lateinit var spinnerFromCurrency: Spinner
    private lateinit var spinnerToCurrency: Spinner
    private lateinit var tvRateFrom: TextView
    private lateinit var tvRateTo: TextView
    private lateinit var btnCreateSwap: Button

    private val currencies = listOf(
        "USD", // Amerika Serikat
        "EUR", // Euro
        "JPY", // Jepang
        "SGD", // Singapura
        "AUD", // Australia
        "GBP", // Inggris
        "IDR", // Indonesia
        "MYR", // Malaysia
        "THB", // Thailand
        "VND", // Vietnam
        "PHP", // Filipina
        "LAK", // Laos
        "INR", // India
        "SAR", // Arab Saudi
        "RUB", // Rusia
        "CNY"  // Cina
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_swap, container, false)

        // Inisialisasi komponen UI
        etFromAmount = view.findViewById(R.id.etFromAmount)
        etToAmount = view.findViewById(R.id.etToAmount)
        spinnerFromCurrency = view.findViewById(R.id.spinnerFromCurrency)
        spinnerToCurrency = view.findViewById(R.id.spinnerToCurrency)
        tvRateFrom = view.findViewById(R.id.tvRateFrom)
        tvRateTo = view.findViewById(R.id.tvRateTo)
        btnCreateSwap = view.findViewById(R.id.btnCreateSwap)

        // Setup adapter untuk spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromCurrency.adapter = adapter
        spinnerToCurrency.adapter = adapter

        // Set default pilihan
        spinnerFromCurrency.setSelection(currencies.indexOf("USD"))
        spinnerToCurrency.setSelection(currencies.indexOf("IDR"))

        // Listener untuk tombol Create Swap
        btnCreateSwap.setOnClickListener {
            val fromCurrency = spinnerFromCurrency.selectedItem.toString()
            val toCurrency = spinnerToCurrency.selectedItem.toString()
            val amountText = etFromAmount.text.toString()

            if (amountText.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null) {
                Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil fungsi untuk mendapatkan kurs
            getExchangeRate(fromCurrency, toCurrency, amount)
        }

        return view
    }

    private fun getExchangeRate(from: String, to: String, amount: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiUrl = "https://api.exchangerate.host/convert?from=$from&to=$to&amount=$amount"
                val response = URL(apiUrl).readText()
                val jsonObject = JSONObject(response)

                val rate = jsonObject.getJSONObject("info").getDouble("rate")
                val result = jsonObject.getDouble("result")

                withContext(Dispatchers.Main) {
                    etToAmount.setText(String.format("%.2f", result))
                    tvRateFrom.text = "1 $from"
                    tvRateTo.text = "= %.2f $to".format(rate)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to retrieve exchange rate", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
