package com.example.mobdeveapplication
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.ProfileBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


private lateinit var binding: ProfileBinding
private lateinit var auth: FirebaseAuth

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        binding = ProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.menu.setGroupCheckable(0,false,true)
        binding.bottomNavigationView.setOnItemSelectedListener{ menu ->
            when (menu.itemId) {
                R.id.homenavbar -> {
                    val homepageIntent = Intent(this, Homepage::class.java)
                    startActivity(homepageIntent)
                    true
                }
                R.id.historynavbar -> {
                    val historyIntent = Intent(this, History::class.java)
                    startActivity(historyIntent)
                    true
                }
                R.id.qrnavbar -> {
                    val qrIntent = Intent(this, QrScanner::class.java)
                    startActivity(qrIntent)
                    true
                }
                R.id.profilenavbar -> {
                    val profileIntent = Intent(this, Profile::class.java)
                    startActivity(profileIntent)
                    true
                }
                R.id.settingsnavbar -> {
                    val settingIntent = Intent(this, Settings::class.java)
                    startActivity(settingIntent)
                    true
                }
                else -> {throw IllegalStateException("something bad happened")}
            }
        }
        val firebaseDatabase = Globals().firebaseDatabase
                    binding.textName.setText(auth.currentUser?.displayName)
                    binding.Greetingbox.text = "Hello ${auth.currentUser?.email}"
        binding.saveName.setOnClickListener {
            firebaseDatabase.getReference("User").child(auth.uid.toString()).child("name").setValue(binding.textName.text.toString()).addOnSuccessListener {
                Toast.makeText(this,"Name change has been saved",Toast.LENGTH_LONG).show()
            }
        }
        binding.savepassword.setOnClickListener {
            auth.sendPasswordResetEmail(auth.currentUser?.email.toString())
            Toast.makeText(this,"An email has been sent to your email", Toast.LENGTH_LONG).show()
        }

        binding.deleteAccount.setOnClickListener {
            firebaseDatabase.getReference("User").child(auth.uid.toString()).removeValue()
            auth.currentUser?.delete()
            auth.signOut()
            val signedout = Intent(this, Registerform::class.java)
            startActivity(signedout)
        }
        Picasso.get().load(auth.currentUser?.photoUrl).fit().into(binding.profilepicture)
    }
}
