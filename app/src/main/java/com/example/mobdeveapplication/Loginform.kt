package com.example.mobdeveapplication
/**
 * @author Quiros, Arnold Luigi G.
 * @author Ty, Sam Allyson O.
 *
 * MOBDEVE S11
 * 16/02/2022
 * Version 1.0
 */
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.ActivityLoginformBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.Exception
import androidx.annotation.NonNull
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


private lateinit var binding : ActivityLoginformBinding

/**
 * Represents the Login screen of the app
 */
class Loginform : AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val universal = Globals()
        auth = universal.auth
        binding = ActivityLoginformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (auth.currentUser != null) {
            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        binding.tvRegisterRedirect.setOnClickListener {
            val intent = Intent(this, Registerform::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSignin.setOnClickListener {
            try {
                if (binding.etEmail.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty())
                    Toast.makeText(this,"Email or Password is not provided",Toast.LENGTH_SHORT).show()
                else{
                    auth.signInWithEmailAndPassword(binding.etEmail.text.toString(),binding.etPassword.text.toString()).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"Sign In successfull. ",Toast.LENGTH_SHORT).show()
                            updateUI()
                        } else
                            Toast.makeText(this,"Invalid Email or Password",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{ Toast.makeText(this, "This Email and password combination does not exist", Toast.LENGTH_LONG).show()}
                    }
            }
            catch(e: Exception){ Toast.makeText(this,"Invalid Email or password. Try Again",Toast.LENGTH_LONG).show() }
        }
    }
    private fun updateUI() {
        val intent = Intent(this, Homepage::class.java)
        startActivity(intent)
        finish()
        }
}
