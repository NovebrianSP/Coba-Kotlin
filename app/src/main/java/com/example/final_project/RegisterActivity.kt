package com.example.final_project

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        dbHelper = DatabaseHelper(this)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextAge = findViewById<EditText>(R.id.editTextAge)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull()
            val password = editTextPassword.text.toString()

            if (name.isNotEmpty() && age != null && password.isNotEmpty()) {
                // Menyimpan data ke database
                saveData(name, age, password)
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                // Lanjutkan ke halaman login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData(name: String, age: Int, password: String) {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseHelper.COL_NAME, name)
        contentValues.put(DatabaseHelper.COL_AGE, age)
        contentValues.put(DatabaseHelper.COL_PASS, password)
        db.insert(DatabaseHelper.TABLE_NAME, null, contentValues)
        db.close()
    }
}
