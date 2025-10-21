package com.example.productlisttestapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.productlisttestapp.R
import com.example.productlisttestapp.model.OrderItem

class ItemAdapter(private val items: List<OrderItem>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvQty: TextView = itemView.findViewById(R.id.tvQty)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.item_name
        holder.tvQty.text = "${item.qty}"
        holder.tvTotal.text = "£ ${String.format("%.2f", item.finalTotal)}"
    }

    override fun getItemCount(): Int = items.size
}
