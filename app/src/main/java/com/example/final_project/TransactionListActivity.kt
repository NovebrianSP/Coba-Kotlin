package com.example.final_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TransactionListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_transaksi)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewTransactions)

        val transactionList = dbHelper.getAllTransactions() // Ganti dengan metode yang sesuai dari DatabaseHelper

        adapter = TransactionAdapter(transactionList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}