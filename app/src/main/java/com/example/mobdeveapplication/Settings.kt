package com.example.mobdeveapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobdeveapplication.databinding.ActivitySettingsBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth

private lateinit var binding: ActivitySettingsBinding


class Settings : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Globals().auth
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.menu.setGroupCheckable(0,false,true)
        binding.bottomNavigationView.setOnItemSelectedListener{ menu ->
            when (menu.itemId) {
                R.id.homenavbar -> {
                    val intentforhomepage = Intent(this, Homepage::class.java)
                    startActivity(intentforhomepage)
                    true
                }
                R.id.historynavbar -> {
                    val intentforhistory = Intent(this, History::class.java)
                    startActivity(intentforhistory)
                    true
                }
                R.id.qrnavbar -> {
                    val intentforqr = Intent(this, QrScanner::class.java)
                    startActivity(intentforqr)
                    true
                }
                R.id.profilenavbar -> {
                    val intent4 = Intent(this, Profile::class.java)
                    startActivity(intent4)
                    true
                }
                R.id.settingsnavbar -> {
                    val intent5 = Intent(this, Settings::class.java)
                    startActivity(intent5)
                    true
                }
                else -> {throw IllegalStateException("something bad happened")}
            }
        }
        binding.savepassword.setOnClickListener {
            auth.sendPasswordResetEmail(auth.currentUser?.email.toString())
            Toast.makeText(this,"An email has been sent to your email", Toast.LENGTH_LONG).show()
        }
        val firebaseDatabase = Globals().firebaseDatabase
        binding.deleteAccount.setOnClickListener {
            firebaseDatabase.getReference("User").child(auth.uid.toString()).removeValue()
            auth.currentUser?.delete()
            auth.signOut()
            val signedout = Intent(this, Registerform::class.java)
            startActivity(signedout)
        }
    }
}