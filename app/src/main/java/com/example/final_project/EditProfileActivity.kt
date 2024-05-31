package com.example.final_project

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSave: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)

        dbHelper = DatabaseHelper(this)

        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSave = findViewById(R.id.buttonSave)

        // Mendapatkan ID akun yang login dari Intent
        val accountId = intent.getIntExtra("ACCOUNT_ID", -1)
        if (accountId == -1) {
            // Handle the case where account ID is not received properly
            Toast.makeText(this, "Invalid account ID", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
        } else {
            // Ambil data profil dari database berdasarkan ID akun yang login dan tampilkan di EditText
            displayProfileData(accountId)

            // Ketika tombol Save ditekan, panggil fungsi untuk menyimpan data
            buttonSave.setOnClickListener {
                saveProfileData(accountId)
            }
        }
    }

    private fun displayProfileData(accountId: Int) {
        val db = dbHelper.readableDatabase
        val selection = "${DatabaseHelper.COL_ID} = ?"
        val selectionArgs = arrayOf(accountId.toString())
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(DatabaseHelper.COL_NAME)
            val ageIndex = cursor.getColumnIndex(DatabaseHelper.COL_AGE)
            val passIndex = cursor.getColumnIndex(DatabaseHelper.COL_PASS)

            if (nameIndex != -1 && ageIndex != -1 && passIndex != -1) {
                val name = cursor.getString(nameIndex)
                val age = cursor.getInt(ageIndex)
                val pass = cursor.getString(passIndex)

                editTextName.setText(name)
                editTextAge.setText(age.toString())
                editTextPassword.setText(pass)
            } else {
                Log.e("DisplayProfileData", "Column indices not found")
                Toast.makeText(this, "Data retrieval error: Column indices not found", Toast.LENGTH_SHORT).show()
            }
        }

        cursor.close()
    }

    private fun saveProfileData(accountId: Int) {
        val name = editTextName.text.toString()
        val age = editTextAge.text.toString().toIntOrNull() ?: 0
        val pass = editTextPassword.text.toString()

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_NAME, name)
            put(DatabaseHelper.COL_AGE, age)
            put(DatabaseHelper.COL_PASS, pass)
        }

        val selection = "${DatabaseHelper.COL_ID} = ?"
        val selectionArgs = arrayOf(accountId.toString())
        val count = db.update(DatabaseHelper.TABLE_NAME, values, selection, selectionArgs)

        if (count > 0) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
        }
    }
}
