package com.example.mobdeveapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobdeveapplication.databinding.RegisterformBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

private lateinit var binding: RegisterformBinding
class Registerform : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
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

        if (auth.currentUser != null) {
            val intent = Intent(this, Homepage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
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
                                binding.Errordisplay.text = "Sign Up successfull. Click Sign in to Continue"
                                val user = auth.currentUser
                                val ref = FirebaseDatabase.getInstance("https://mobdeve-application-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User")
                                    ref.child(auth.currentUser!!.uid)
                                val profileupdate = userProfileChangeRequest {
                                    displayName = binding.namebox.text.toString()
                                    photoUri = Uri.parse("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__480.png")
                                }
                                user?.updateProfile(profileupdate)!!
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
                    val email = binding.emailbox.text.toString()
                    val access = "False"
                    adminaccess(email, access)
                }
        }
    }
    fun updateUI(currentUser: FirebaseUser?) {
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
