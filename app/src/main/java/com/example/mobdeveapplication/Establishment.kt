package com.example.mobdeveapplication
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.EstablishmentBinding
import com.example.mobdeveapplication.databinding.ProfileBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.profile.*
import androidx.annotation.NonNull
import androidx.browser.browseractions.BrowserActionsIntent

import com.google.android.gms.tasks.OnFailureListener

import androidx.browser.customtabs.CustomTabsIntent.KEY_DESCRIPTION

import androidx.browser.browseractions.BrowserActionsIntent.KEY_TITLE
import androidx.browser.customtabs.CustomTabsIntent

import com.google.firebase.firestore.DocumentSnapshot

import com.google.android.gms.tasks.OnSuccessListener
import com.squareup.picasso.Picasso


private lateinit var binding: EstablishmentBinding
private lateinit var auth: FirebaseAuth

class Establishment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        val firestore = universal.db
        super.onCreate(savedInstanceState)
        binding = EstablishmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore.collection("Establishments").document("Gu1KS7kZbW21BIaTOiCV").get()
            .addOnSuccessListener { document ->
                binding.Titletext.text = document.data!!["Name"].toString()
                binding.descriptiontxt.text = document.data!!["About"].toString()
                Picasso.get().load(document.data!!["link"].toString()).into(binding.imageView)

            }
    }
}

