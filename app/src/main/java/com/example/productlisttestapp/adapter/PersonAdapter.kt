package com.example.productlisttestapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.productlisttestapp.R
import com.example.productlisttestapp.model.Person

class PersonAdapter(private val persons: MutableList<Person>) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvPersonName)
        val txtAmount: TextView = itemView.findViewById(R.id.txtAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.person_row, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = persons[position]
        holder.tvName.text = person.name
        holder.txtAmount.setText("£ "
                +person.amount.toString())

        holder.txtAmount.addTextChangedListener {
            val value = it.toString().toDoubleOrNull() ?: 0.0
            person.amount = value
        }
    }

    override fun getItemCount() = persons.size
}

