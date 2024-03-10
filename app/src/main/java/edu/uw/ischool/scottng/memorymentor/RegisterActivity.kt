package edu.uw.ischool.scottng.memorymentor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val buttonRegister: Button = findViewById(R.id.buttonRegister)
        val editTextEmail: EditText = findViewById(R.id.editTextEmail)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)

        buttonRegister.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                createUser(email, password)
            } else {
                Toast.makeText(this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Registration successful.", Toast.LENGTH_SHORT).show()
                    finish() // Optionally, navigate back to login or main activity
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
