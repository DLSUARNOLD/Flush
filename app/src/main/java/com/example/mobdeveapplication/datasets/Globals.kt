package com.example.mobdeveapplication.datasets

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Globals {
    var auth: FirebaseAuth = Firebase.auth
    val firebaseDatabase = FirebaseDatabase.getInstance("https://mobdeve-application-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val db = Firebase.firestore
}