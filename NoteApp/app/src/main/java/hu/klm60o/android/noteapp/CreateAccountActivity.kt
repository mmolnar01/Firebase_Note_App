package hu.klm60o.android.noteapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hu.klm60o.android.noteapp.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_create_account)
        setContentView(binding.root)

        binding.createAccountButton.setOnClickListener {
            createAccount()
        }

        binding.loginTextViewButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        /*val currentUser = auth.currentUser
        if (currentUser != null) {
            //reload()
        }*/
    }

    private fun createAccount() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword = binding.passwordConfirmEditText.text.toString()

        val accountValidated = validateData(email, password, confirmPassword)

        if (!accountValidated) {
            return
        }

        createAccountInFirebase(email, password)
    }

    private fun validateData(email: String, password: String, confirmPassword: String): Boolean {
        //Validating the email address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Invalid Email"
            return false
        }
        //Validating the password
        if (password.length < 5) {
            binding.passwordEditText.error = "Password should be at least 5 cahracters"
            return false
        }
        if (!password.equals(confirmPassword)) {
            binding.passwordConfirmEditText.error = "Passwords do not match"
            return false
        }

        return true
    }

    private fun createAccountInFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")
                Toast.makeText(
                    this,
                    "Account Created Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                auth.currentUser?.sendEmailVerification()
                auth.signOut()
                finish()
                //updateUI(user)
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
                //updateUI(null)
            }
        }
    }
}