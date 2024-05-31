package com.example.final_project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var imageButtonProfile: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val textWelcomeMessage = findViewById<TextView>(R.id.textWelcomeMessage)
        val imageButtonCreateTransaction = findViewById<ImageButton>(R.id.imageButtonCreateTransaction)
        val imageButtonViewTransactions = findViewById<ImageButton>(R.id.imageButtonViewTransactions)
        val imageButtonLogout = findViewById<ImageButton>(R.id.imageButtonLogout)

        // Mendapatkan nama akun yang login (di sini, diasumsikan menggunakan Intent dari LoginActivity)
        val username = intent.getStringExtra("USERNAME")

        // Menampilkan pesan selamat datang dengan nama akun
        textWelcomeMessage.text = "Selamat Datang, $username"

        // Menangani klik ImageButton "Buat Transaksi"
        imageButtonCreateTransaction.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        // Menangani klik ImageButton "Lihat Transaksi"
        imageButtonViewTransactions.setOnClickListener {
            val intent = Intent(this, TransactionListActivity::class.java)
            startActivity(intent)
        }

        // Menangani klik ImageButton "Logout"
        imageButtonLogout.setOnClickListener {
            // Kembali ke halaman login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Menutup activity home setelah logout
        }
        imageButtonProfile = findViewById(R.id.imageButtonProfile)
        imageButtonProfile.setOnClickListener {
            // Ketika ImageButton ditekan, buka halaman edit profil di sini
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
