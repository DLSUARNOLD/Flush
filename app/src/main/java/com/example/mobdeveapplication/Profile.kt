package com.example.mobdeveapplication
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.ProfileBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.profile.*


private lateinit var binding: ProfileBinding
private lateinit var auth: FirebaseAuth

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        binding = ProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var email = ""
        //Log.i("firebase", "Got value $user")
        val firebaseDatabase = FirebaseDatabase.getInstance("https://mobdeve-application-default-rtdb.asia-southeast1.firebasedatabase.app/")
        var databaseReference = firebaseDatabase.getReference("User").child(auth.uid.toString()).get().addOnSuccessListener{
                    text_name.setText(it.child("name").value as String)
                    email = it.child("email").value as String
                    binding.Greetingbox.text = "Hello ${email}"
        }
        save_name.setOnClickListener {
            var namereference = firebaseDatabase.getReference("User").child(auth.uid.toString()).child("name").setValue(binding.textName.text.toString()).addOnSuccessListener {
                Toast.makeText(this,"Name change has been saved",Toast.LENGTH_LONG).show()
            }
        }
        savepassword.setOnClickListener {
            auth.sendPasswordResetEmail(email)
            Toast.makeText(this,"An email has been sent to your email",Toast.LENGTH_LONG).show()
        }

    }
}
