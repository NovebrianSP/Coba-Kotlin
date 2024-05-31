package com.example.final_project

import android.R
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val mContext: Context = context

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NAME " +
                "($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_NAME TEXT, $COL_AGE INTEGER, $COL_PASS TEXT);"

        val createTransactionTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_TRANSACTION " +
                "($COL_TRANSACTION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_TRANSACTION_DATE TEXT, " +
                "$COL_TRANSACTION_AMOUNT REAL, " +
                "$COL_TRANSACTION_TYPE TEXT);"

        db.execSQL(createTableQuery)
        db.execSQL(createTransactionTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aksi jika ada upgrade versi database
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTION")
        onCreate(db)
    }

    companion object {
        val DATABASE_NAME = "emprit"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "akun"
        val COL_ID = "id"
        val COL_NAME = "name"
        val COL_AGE = "age"
        val COL_PASS = "pass"

        val TABLE_TRANSACTION = "transactions"

        // Kolom-kolom yang diperlukan dalam tabel transaksi
        val COL_TRANSACTION_ID = "transaction_id"
        val COL_TRANSACTION_DATE = "transaction_date"
        val COL_TRANSACTION_AMOUNT = "transaction_amount"
        val COL_TRANSACTION_TYPE = "transaction_type"
    }

    data class TransactionDetails(
        val id: Int,
        val date: String,
        val amount: Double,
        val type: String
    )

    fun showAllAccounts() {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val accountList = mutableListOf<String>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME))
            val age = cursor.getInt(cursor.getColumnIndexOrThrow(COL_AGE))
            val pass = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASS))

            accountList.add("ID: $id\nName: $name\nAge: $age\nPassword: $pass")
        }
        cursor.close()

        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Daftar Semua Akun")
        builder.setAdapter(ArrayAdapter(mContext, R.layout.simple_list_item_1, accountList)) { dialog, position ->
            val accountId = accountList[position].split("\n")[0].split(":")[1].trim().toInt()
            confirmDeleteAccount(accountId)
            dialog.dismiss()
        }
        builder.show()
    }

    private fun confirmDeleteAccount(accountId: Int) {
        val confirmDeleteBuilder = AlertDialog.Builder(mContext)
        confirmDeleteBuilder.setTitle("Delete Account")
        confirmDeleteBuilder.setMessage("Are you sure you want to delete this account?")
        confirmDeleteBuilder.setPositiveButton("Yes") { _, _ ->
            deleteAccount(accountId)
        }
        confirmDeleteBuilder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        val confirmDeleteDialog = confirmDeleteBuilder.create()
        confirmDeleteDialog.show()
    }

    private fun deleteAccount(accountId: Int) {
        val db = writableDatabase
        val selection = "$COL_ID = ?"
        val selectionArgs = arrayOf(accountId.toString())
        val deletedRows = db.delete(TABLE_NAME, selection, selectionArgs)

        if (deletedRows > 0) {
            Toast.makeText(mContext, "Account deleted successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(mContext, "Failed to delete account", Toast.LENGTH_SHORT).show()
        }
    }

        fun getAllTransactions(): List<TransactionDetails> {
            val transactionList = mutableListOf<TransactionDetails>()
            val db = readableDatabase

            val query = "SELECT * FROM $TABLE_TRANSACTION"
            val cursor: Cursor? = db.rawQuery(query, null)

            cursor?.use {
                val idIndex = it.getColumnIndex(COL_TRANSACTION_ID)
                val dateIndex = it.getColumnIndex(COL_TRANSACTION_DATE)
                val amountIndex = it.getColumnIndex(COL_TRANSACTION_AMOUNT)
                val typeIndex = it.getColumnIndex(COL_TRANSACTION_TYPE)

                while (it.moveToNext()) {
                    val id = it.getInt(idIndex)
                    val date = it.getString(dateIndex)
                    val amount = it.getDouble(amountIndex)
                    val type = it.getString(typeIndex)

                    val transaction = TransactionDetails(id, date, amount, type)
                    transactionList.add(transaction)
                }
            }

            cursor?.close()
            return transactionList
        }

    fun saveTransaction(transactionDetails: TransactionDetails) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_TRANSACTION_DATE, transactionDetails.date)
            put(COL_TRANSACTION_AMOUNT, transactionDetails.amount)
            put(COL_TRANSACTION_TYPE, transactionDetails.type)
        }

        val newRowId = db.insert(TABLE_TRANSACTION, null, contentValues)

        if (newRowId == -1L) {
            // Penanganan kegagalan penyimpanan
            Log.e("DatabaseHelper", "Gagal menyimpan transaksi ke database")
            // Misalnya, tampilkan pesan kesalahan atau lakukan tindakan lain yang diperlukan
        } else {
            // Penyimpanan berhasil
            Log.d("DatabaseHelper", "Transaksi berhasil disimpan. ID baru: $newRowId")
        }
    }

    fun getAccountIdFromDatabase(name: String, password: String): Int {
        val db = readableDatabase
        val selection = "${DatabaseHelper.COL_NAME} = ? AND ${DatabaseHelper.COL_PASS} = ?"
        val selectionArgs = arrayOf(name, password)
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COL_ID),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var accountId = -1
        if (cursor.moveToFirst()) {
            accountId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID))
        }

        cursor.close()
        return accountId
    }
}