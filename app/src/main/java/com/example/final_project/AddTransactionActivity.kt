package com.example.final_project

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var editTextAmount: EditText
    private lateinit var spinnerTransactionType: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transaksi)

        dbHelper = DatabaseHelper(this)
        editTextAmount = findViewById(R.id.editTextAmount)
        spinnerTransactionType = findViewById(R.id.spinnerTransactionType)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.transaction_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTransactionType.adapter = adapter

        val buttonSaveTransaction: Button = findViewById(R.id.buttonSaveTransaction)
        buttonSaveTransaction.setOnClickListener {
            saveTransaction()
        }
    }

    private fun saveTransaction() {
        val amountText = editTextAmount.text.toString()
        val type = spinnerTransactionType.selectedItem.toString()

        if (amountText.isNotEmpty()) {
            val amount = amountText.toDoubleOrNull() ?: 0.0
            val currentDate = getCurrentDateTime()
            val transactionDetails = DatabaseHelper.TransactionDetails(0, currentDate, amount, type)

            dbHelper.saveTransaction(transactionDetails)
            finish()
        } else {
            editTextAmount.error = "Please enter amount"
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}