package com.example.final_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project.DatabaseHelper.TransactionDetails
import com.example.final_project.R

class TransactionAdapter(private val transactionList: List<TransactionDetails>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        val amountTextView: TextView = itemView.findViewById(R.id.textViewAmount)
        val typeTextView: TextView = itemView.findViewById(R.id.textViewType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.dateTextView.text = "Date: ${transaction.date}"
        holder.amountTextView.text = "Amount: ${transaction.amount}"
        holder.typeTextView.text = "Type: ${transaction.type}"
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
