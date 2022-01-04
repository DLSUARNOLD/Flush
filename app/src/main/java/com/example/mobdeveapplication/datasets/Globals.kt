package com.example.mobdeveapplication.datasets

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Globals {
    var auth: FirebaseAuth = Firebase.auth
}