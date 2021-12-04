package com.example.mobdeveapplication
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.HomepageBinding


private lateinit var binding: HomepageBinding
class Homepage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val receivedEmail = intent.getStringExtra("emailAddress")
        binding.textViewWelcome.text="Welcome "+receivedEmail
    }
}
