package com.example.mobdeveapplication
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import com.example.mobdeveapplication.dataLoginformBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.loginform.*
import kotlinx.android.synthetic.main.loginform.Errordisplay
import kotlinx.android.synthetic.main.loginform.Passwordbox
import kotlinx.android.synthetic.main.loginform.Usernamebox
import kotlinx.android.synthetic.main.registerform.*

class Loginform : AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    //private lateinit var binding : LoginformBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.loginform)
        registerredirect.setOnClickListener {
            val intent = Intent(this, Registerform::class.java)
            startActivity(intent)
            finish()
        }
        SigninButton.setOnClickListener {
            val inputMethodManager =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            if (Usernamebox.text.toString().isNullOrEmpty() || Passwordbox.text.toString().isNullOrEmpty())
                Errordisplay.text = "Email Address or Password is not provided"
            else {
                auth.signInWithEmailAndPassword(Usernamebox.text.toString(),Passwordbox.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Errordisplay.text =  "Sign In successfull. "
                            val user = auth.currentUser
                            updateUI(user, Usernamebox.text.toString())
                        } else
                            Errordisplay.text = "Invalid Email or Password"
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