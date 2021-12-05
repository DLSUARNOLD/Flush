package com.example.mobdeveapplication
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.HomepageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


private lateinit var binding: HomepageBinding
private lateinit var auth: FirebaseAuth
class Homepage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val receivedEmail = intent.getStringExtra("emailAddress")
        binding.textViewWelcome.text="Welcome "+receivedEmail

        binding.profileBtn.setOnClickListener{
            updateUI(user)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser !=null){
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()
        }
    }
}
