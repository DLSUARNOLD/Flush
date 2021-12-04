package com.example.mobdeveapplication
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.LoginformBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private lateinit var binding : LoginformBinding
class Loginform : AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = LoginformBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerredirect.setOnClickListener {
            val intent = Intent(this, Registerform::class.java)
            startActivity(intent)
            finish()
        }
        binding.SigninButton.setOnClickListener {
            //val inputMethodManager =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            if (binding.Usernamebox.text.toString().isEmpty() || binding.Passwordbox.text.toString().isEmpty())
                binding.Errordisplay.text = "Email Address or Password is not provided"
            else {
                auth.signInWithEmailAndPassword(binding.Usernamebox.text.toString(),binding.Passwordbox.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            binding.Errordisplay.text =  "Sign In successfull. "
                            val user = auth.currentUser
                            updateUI(user, binding.Usernamebox.text.toString())
                        } else
                            binding.Errordisplay.text = "Invalid Email or Password"
                    }
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?, emailAdd: String) {
        if(currentUser !=null){
            val intent = Intent(this, Homepage::class.java)
            intent.putExtra("emailAddress", emailAdd);
            startActivity(intent)
            finish()
        }
    }
}