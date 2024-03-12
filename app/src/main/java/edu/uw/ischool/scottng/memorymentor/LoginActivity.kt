package edu.uw.ischool.scottng.memorymentor

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_EMAIL_KEY = "USER_EMAIL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()

        val buttonLogin: Button = findViewById(R.id.buttonLogin)
        val editTextEmail: EditText = findViewById(R.id.editTextLoginEmail)
        val editTextPassword: EditText = findViewById(R.id.editTextLoginPassword)
        val textViewGoToRegister: TextView = findViewById(R.id.textViewGoToRegister)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val userEmail = email.replace(".", "_")
                saveEmailToPreferences(userEmail)
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }

        textViewGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, CategoryActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Incorrect email or password!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Save email to SharedPreferences
    private fun saveEmailToPreferences(email: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_EMAIL_KEY, email)
        editor.apply()
    }
}