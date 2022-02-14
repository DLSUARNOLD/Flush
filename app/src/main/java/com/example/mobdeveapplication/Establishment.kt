package com.example.mobdeveapplication
import android.R.attr
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.EstablishmentBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import android.R.attr.tag
import android.annotation.SuppressLint

import android.content.Intent
import android.net.Uri


private lateinit var binding: EstablishmentBinding
private lateinit var auth: FirebaseAuth

class Establishment : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        val firestore = universal.db
        super.onCreate(savedInstanceState)
        binding = EstablishmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        firestore.collection("Establishments").document("Gu1KS7kZbW21BIaTOiCV").get()
            .addOnSuccessListener { document ->
                binding.Titletext.text = document.data!!["Name"].toString()
                binding.descriptiontxt.text = document.data!!["About"].toString()
                Picasso.get().load(document.data!!["link"].toString()).into(binding.imageView)
            }*/
        val intent = intent
        binding.Titletext.text = intent.getStringExtra("name")
        binding.descriptiontxt.text = intent.getStringExtra("description")
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

