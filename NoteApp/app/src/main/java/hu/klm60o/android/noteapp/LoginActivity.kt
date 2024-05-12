package hu.klm60o.android.noteapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hu.klm60o.android.noteapp.databinding.ActivityCreateAccountBinding
import hu.klm60o.android.noteapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAnalytics = Firebase.analytics

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.loginTextViewButton.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
    }

    private fun loginUser() {
        val email = binding.emailEditTextLogin.text.toString()
        val password = binding.passwordEditTextLogin.text.toString()

        val isValidated = validateData(email, password)

        if (!isValidated) {
            return
        }

        loginToFirebase(email, password)
    }

    private fun validateData(email: String, password: String): Boolean {
        //Validating the email address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditTextLogin.error = "Invalid Email"
            return false
        }
        //Validating the password
        if (password.length < 5) {
            binding.passwordEditTextLogin.error = "Password should be at least 5 characters"
            return false
        }

        return true
    }

    private fun loginToFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified() == true) {
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalytics.Param.METHOD, "email")
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            this,
                            "Email not verified",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }
    }
}