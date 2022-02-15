package com.example.mobdeveapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobdeveapplication.databinding.ActivityRegisterformBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

private lateinit var binding: ActivityRegisterformBinding
class Registerform : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?)
    {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterformBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.LoginRedirect.setOnClickListener {
            val intent = Intent(this, Loginform::class.java)
            startActivity(intent)
            finish()
             }

        if (auth.currentUser != null) {
            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        binding.createAccount.setOnClickListener{
            if (binding.Emailbox.text.toString().isEmpty() || binding.Passwordbox.text.toString().isEmpty() || binding.Namebox.text.toString().isEmpty())
                binding.Errordisplay.text = "Email Address or Password is not provided"
            else {
                    auth.createUserWithEmailAndPassword(binding.Emailbox.text.toString(), binding.Passwordbox.text.toString()).addOnCompleteListener(this)
                    { task ->
                                if (task.isSuccessful) {
                                    binding.Errordisplay.text = "Sign Up successful. Click Sign in to Continue"
                                    val user = auth.currentUser
                                    val ref = FirebaseDatabase.getInstance("https://mobdeve-application-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User")
                                        ref.child(auth.currentUser!!.uid)
                                    val profileupdate = userProfileChangeRequest {
                                        displayName = binding.Namebox.text.toString()
                                        photoUri = Uri.parse("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__480.png")

                                    val email = binding.Emailbox.text.toString()
                                    val access = "No"
                                    adminaccess(email, access)
                                }
                                user?.updateProfile(profileupdate)!!
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

    fun adminaccess(email: String, access: String)
    {
        val db = Globals().db
        val admin: MutableMap<String, Any> = HashMap()
        admin["access"] = access
        admin["email"] = email

        db.collection("AdminAccess")
            .add(admin)
    }
}
