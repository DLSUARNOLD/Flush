package com.example.mobdeveapplication
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
        binding.registerredirect.setOnClickListener {
            val intent = Intent(this, Registerform::class.java)
            startActivity(intent)
            finish()
        }
        binding.SigninButton.setOnClickListener {
            try {
                if (binding.Emailbox.text.toString().isEmpty() || binding.Passwordbox.text.toString().isEmpty())
                    binding.Errordisplay.text = "Email or Password is not provided"
                else{
                    auth.signInWithEmailAndPassword(binding.Emailbox.text.toString(),binding.Passwordbox.text.toString()).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            binding.Errordisplay.text = "Sign In successfull. "
                            updateUI()
                        } else
                            binding.Errordisplay.text = "Invalid Email or Password"
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
