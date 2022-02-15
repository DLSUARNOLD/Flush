package com.example.mobdeveapplication
import android.R.attr
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.ActivityEstablishmentBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import android.R.attr.tag
import android.annotation.SuppressLint

import android.content.Intent
import android.net.Uri


private lateinit var binding: ActivityEstablishmentBinding
private lateinit var auth: FirebaseAuth

class Establishment : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        val firestore = universal.db
        super.onCreate(savedInstanceState)
        binding = ActivityEstablishmentBinding.inflate(layoutInflater)
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
                else -> {throw IllegalStateException("Error")}
            }
        }

        val intent = intent
        binding.Titletext.text = intent.getStringExtra("name")
        binding.descriptiontext.text = intent.getStringExtra("description")
        Picasso.get().load(intent.getStringExtra("picture")).fit().into(binding.imageView)
        binding.ratingbar.rating = intent.getDoubleExtra("rating", 0.0).toFloat()

        var directionslink = ""
        firestore.collection("Establishments").whereEqualTo("Name", binding.Titletext.text).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    directionslink = document.data["Location"].toString()
                }
                binding.directionsbutton.setOnClickListener {
                    val mapsintent = Intent(Intent.ACTION_VIEW, Uri.parse(directionslink))
                    startActivity(mapsintent)
                }
            }
    }
}

