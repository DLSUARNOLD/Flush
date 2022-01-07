package com.example.mobdeveapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mobdeveapplication.databinding.RegisterformBinding
import com.example.mobdeveapplication.datasets.Globals
import com.example.mobdeveapplication.datasets.UserC
//import com.example.mobdeveapplication.dataRegisterformBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.registerform.*

private lateinit var binding: RegisterformBinding
class Registerform : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        binding = RegisterformBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.LoginRedirect.setOnClickListener {
            val intent = Intent(this, Loginform::class.java)
            startActivity(intent)
            finish()
             }


        binding.Submitbutt.setOnClickListener{
            //val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            if (binding.emailbox.text.toString().isEmpty() || binding.Passwordbox.text.toString().isEmpty() || binding.emailbox.text.toString().isEmpty())
                binding.Errordisplay.text = "Email Address or Password is not provided"
            else {
                    auth.createUserWithEmailAndPassword(binding.emailbox.text.toString(), binding.Passwordbox.text.toString()).addOnCompleteListener(this)
                    { task ->
                                if (task.isSuccessful) {
                                binding.Errordisplay.text = "Sign Up successfull. Email and Password created"
                                val user = auth.currentUser
                                val profileupdate = userProfileChangeRequest {displayName = binding.namebox.text.toString()}
                                user?.updateProfile(profileupdate)!!
                                val ref = FirebaseDatabase.getInstance("https://mobdeve-application-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User")
                                    ref.child(auth.currentUser!!.uid).setValue("").addOnCompleteListener(this){
                                        Toast.makeText(this,"User Saved", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener(this) {
                                        Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
                                    }
                                //updateUI(user)
                            } else
                            {
                                if(binding.Passwordbox.length()<6)
                                {
                                    binding.Errordisplay.text = "Sign Up Error: Password must be atleast 6 characters"
                                }
                                else binding.Errordisplay.text = "Sign Up Error: Please chose a different Email"
                            }
                    }
                }
        }
    }
}
