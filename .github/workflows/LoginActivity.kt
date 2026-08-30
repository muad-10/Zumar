package com.zumar.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zumar.app.util.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)

        val etEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val tvError = findViewById<TextView>(R.id.tvLoginError)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvGoRegister = findViewById<TextView>(R.id.tvGoRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showError(tvError, "Please enter your email and password.")
                return@setOnClickListener
            }

            val user = session.login(email, password)
            if (user == null) {
                showError(tvError, "Incorrect email or password, or no account yet.")
            } else {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        }

        tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showError(tv: TextView, message: String) {
        tv.text = message
        tv.visibility = TextView.VISIBLE
    }
}
