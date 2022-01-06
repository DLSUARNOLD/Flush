package com.example.mobdeveapplication
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.HomepageBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


private lateinit var binding: HomepageBinding
private lateinit var auth: FirebaseAuth
class Homepage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val universal = Globals()
        auth = universal.auth
        binding = HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewWelcome.text="Welcome ${auth.currentUser?.displayName}"
        binding.profileBtn.setOnClickListener{
            val profileintent = Intent(this,Profile::class.java)
            startActivity(profileintent)
        }
        binding.historyBtn.setOnClickListener{
            val historyintent = Intent(this,History::class.java)
            startActivity(historyintent)
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
