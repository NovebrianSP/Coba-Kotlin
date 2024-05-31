package com.example.final_project

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var editTextLoginName: EditText
    private lateinit var editTextLoginPassword: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        dbHelper = DatabaseHelper(this)

        editTextLoginName = findViewById(R.id.editTextLoginName)
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textRegister = findViewById<TextView>(R.id.textRegister)

        buttonLogin.setOnClickListener {
            val name = editTextLoginName.text.toString()
            val password = editTextLoginPassword.text.toString()

            if (name.isNotEmpty() && password.isNotEmpty()) {
                if (validateLogin(name, password)) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    navigateToHome(name) // Navigasi ke com.example.final_project.HomeActivity setelah login berhasil
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        val buttonShowAccounts = findViewById<TextView>(R.id.buttonShowAccounts)
        buttonShowAccounts.setOnClickListener {
            showAllAccounts()
        }

        textRegister.setOnClickListener {
            navigateToRegister() // Navigasi ke RegisterActivity saat memilih untuk register
        }
    }

    private fun validateLogin(name: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val selection = "${DatabaseHelper.COL_NAME} = ? AND ${DatabaseHelper.COL_PASS} = ?"
        val selectionArgs = arrayOf(name, password)
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val count: Int = cursor.count
        cursor.close()

        if (count > 0) {
            return true // Login berhasil
        } else {
            return false // Login gagal
        }
    }

    private fun navigateToHome(username: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("USERNAME", username)
        startActivity(intent)
        finish() // Menutup LoginActivity setelah berhasil login
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun showAllAccounts() {
        dbHelper.showAllAccounts()
    }

    private fun navigateToEditProfile(accountId: Int) {
        val intent = Intent(this, EditProfileActivity::class.java)
        intent.putExtra("ACCOUNT_ID", accountId)
        startActivity(intent)
    }

    // Di tempat lain dalam LoginActivity
//    val name = editTextLoginName.text.toString()
//    val password = editTextLoginPassword.text.toString()
//
//    if (validateLogin(name, password)) {
//        // Login berhasil, navigasi ke EditProfileActivity
//        val accountId = dbHelper.getAccountIdFromDatabase(name, password)
//        navigateToEditProfile(accountId)
//    } else {
//        // Login gagal, tampilkan pesan kesalahan atau lakukan tindakan lain yang sesuai
//        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
//    }
}