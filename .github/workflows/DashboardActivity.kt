package com.zumar.app

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zumar.app.util.SessionManager
import java.text.NumberFormat
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var session: SessionManager
    private lateinit var tvBalance: TextView
    private var balance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        session = SessionManager(this)
        val user = session.getCurrentUser()

        if (user == null) {
            // No session found — send back to login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        balance = user.balance

        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        tvBalance = findViewById(R.id.tvBalance)
        val tvCustomerService = findViewById<TextView>(R.id.tvCustomerService)
        val btnFundAccount = findViewById<Button>(R.id.btnFundAccount)
        val actionAirtime = findViewById<android.widget.LinearLayout>(R.id.actionAirtime)
        val actionData = findViewById<android.widget.LinearLayout>(R.id.actionData)

        tvGreeting.text = "Hi, ${user.firstName}"
        updateBalanceDisplay()

        btnFundAccount.setOnClickListener { showFundDialog() }

        tvCustomerService.setOnClickListener {
            startActivity(Intent(this, CustomerServiceActivity::class.java))
        }

        actionAirtime.setOnClickListener {
            Toast.makeText(this, "Buy Airtime is coming soon on Zumar.", Toast.LENGTH_SHORT).show()
        }
        actionData.setOnClickListener {
            Toast.makeText(this, "Buy Data is coming soon on Zumar.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showFundDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_fund_wallet)
        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_card_rounded)

        val etAmount = dialog.findViewById<EditText>(R.id.etFundAmount)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnConfirmFund)

        btnConfirm.setOnClickListener {
            val raw = etAmount.text.toString().trim()
            val amount = raw.toDoubleOrNull()

            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Enter a valid amount.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            balance += amount
            session.updateBalance(balance)
            updateBalanceDisplay()
            dialog.dismiss()
            Toast.makeText(this, "₦${format(amount)} added to your wallet!", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    private fun updateBalanceDisplay() {
        tvBalance.text = "₦${format(balance)}"
    }

    private fun format(value: Double): String {
        val nf = NumberFormat.getNumberInstance(Locale("en", "NG"))
        nf.minimumFractionDigits = 2
        nf.maximumFractionDigits = 2
        return nf.format(value)
    }
}
