package com.example.myswap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myswap.R
import com.example.myswap.data.SwapHistory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class SwapHistoryAdapter(originalList: List<SwapHistory>) :
    RecyclerView.Adapter<SwapHistoryAdapter.ViewHolder>() {

    // Filter hanya item yang fromAmount dan toAmount bukan 0.0
    private val list = originalList.filter { it.fromAmount != 0.0 && it.toAmount != 0.0 }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fromText: TextView = view.findViewById(R.id.tvFromCurrency)
        val toText: TextView = view.findViewById(R.id.tvToCurrency)
        val amountText: TextView = view.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_swap_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // Format angka dengan titik ribuan dan koma desimal (Indonesia)
        val formatter = NumberFormat.getNumberInstance(Locale("in", "ID")).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }

        val formattedFrom = formatter.format(item.fromAmount)
        val formattedTo = formatter.format(item.toAmount)

        // Format waktu transaksi
        val formattedTime =
            SimpleDateFormat("HH:mm:ss", Locale("in", "ID")).format(Date(item.timestamp))

        holder.fromText.text = "$formattedFrom ${item.fromCurrency}"
        holder.toText.text = "$formattedTo ${item.toCurrency}"
        holder.amountText.text = "Waktu: $formattedTime"
    }
}
