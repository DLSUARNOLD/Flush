package com.example.mobdeveapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
//import com.example.mobdeveapplication.dataRegisterformBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser


import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.loginform.*
import kotlinx.android.synthetic.main.registerform.*
import kotlinx.android.synthetic.main.registerform.Errordisplay
import kotlinx.android.synthetic.main.registerform.Passwordbox
import kotlinx.android.synthetic.main.registerform.Usernamebox


class Registerform : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    //private lateinit var binding: RegisterformBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registerform)

        LoginRedirect.setOnClickListener {
            val intent = Intent(this, Loginform::class.java)
            startActivity(intent)
            finish()
             }


        Submitbutt.setOnClickListener{
            //val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            if (Usernamebox.text.toString().isEmpty() || Passwordbox.text.toString()
                    .isEmpty())
                Errordisplay.text = "Email Address or Password is not provided"
            else {
                    auth.createUserWithEmailAndPassword(Usernamebox.text.toString(), Passwordbox.text.toString()).addOnCompleteListener(this)
                    { task ->

                            if (task.isSuccessful) {
                                Errordisplay.text = "Sign Up successfull. Email and Password created"
                                val user = auth.currentUser
                                var account = UserC(namebox.text.toString(),Usernamebox.text.toString())
                                val ref = FirebaseDatabase.getInstance("https://mobdeve-application-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User")
                                val id = ref.push().key
                                if (id != null) {
                                    ref.child(id).setValue(account).addOnCompleteListener(this){
                                        Toast.makeText(this,"User Saved", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener(this) {
                                        Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                updateUI(user)
                            } else
                            {
                                if(Passwordbox.length()<6)
                                {
                                    Errordisplay.text = "Sign Up Error: Password must be atleast 6 characters"
                                }
                                else Errordisplay.text = "Sign Up Error: Please chose a different Email"
                            }
                    }
                }
        }
    }
    fun updateUI(currentUser: FirebaseUser?) {
    }
}
