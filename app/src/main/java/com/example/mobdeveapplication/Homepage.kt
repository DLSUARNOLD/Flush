package com.example.mobdeveapplication
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.HomepageBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


private lateinit var binding: HomepageBinding
private lateinit var auth: FirebaseAuth
class Homepage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var universal = Globals()
        auth = universal.auth
        binding = HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val receivedUser = intent.getStringExtra("username")
        binding.textViewWelcome.text="Welcome "+receivedUser
        binding.profileBtn.setOnClickListener{
            val userid = intent.getStringExtra("userid")
            val profileintent = Intent(this,Profile::class.java)
            profileintent.putExtra("username",receivedUser)
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
