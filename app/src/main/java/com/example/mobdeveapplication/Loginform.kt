package com.example.mobdeveapplication
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.LoginformBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

private lateinit var binding : LoginformBinding
class Loginform : AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var universal = Globals()
        auth = universal.auth
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

            val firebaseDatabase = FirebaseDatabase.getInstance("https://mobdeve-application-default-rtdb.asia-southeast1.firebasedatabase.app/")
            var databaseReference = firebaseDatabase.getReference("User").child(binding.Usernamebox.text.toString()).child("email").get().addOnSuccessListener{
                val email = it.value
                if (binding.Usernamebox.text.toString().isEmpty() || binding.Passwordbox.text.toString().isEmpty())
                    binding.Errordisplay.text = "Email Address or Password is not provided"
                else {
                    auth.signInWithEmailAndPassword(email as String,binding.Passwordbox.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                binding.Errordisplay.text =  "Sign In successfull. "
                                val user = auth.currentUser
                                updateUI(user, binding.Usernamebox.text.toString())
                            } else
                                binding.Errordisplay.text = "Invalid Email or Password"
                        }
                }
            }.addOnFailureListener{
                Toast.makeText(this,"This username and password combination does not exist",Toast.LENGTH_LONG).show()
            }



        }
    }

    private fun updateUI(currentUser: FirebaseUser?, username: String) {
        if(currentUser !=null){
            val intent = Intent(this, Homepage::class.java)
            intent.putExtra("username", username)
            //Log.i("firebase", "Got value $currentUser")
            startActivity(intent)
            finish()
        }
    }
}