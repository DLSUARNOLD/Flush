package com.example.mobdeveapplication
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.ActivityRatingScreenBinding
import com.example.mobdeveapplication.datasets.Adapter
import com.example.mobdeveapplication.datasets.Globals
import com.example.mobdeveapplication.datasets.Historyobject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso



private lateinit var binding: ActivityRatingScreenBinding
private lateinit var auth: FirebaseAuth

class RatingScreen : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        binding = ActivityRatingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val db = universal.db
        db.collection("Establishments").document(intent.getStringExtra("establishment").toString())
            .get().addOnSuccessListener { documents ->
            binding.Titletext.text = documents.data!!["Name"].toString()
            Picasso.get().load(documents.data!!["link"].toString()).fit().into(binding.imageView)
            binding.tvRatingquestion.text =
                "How would you rate your experience with " + documents.data!!["Name"].toString()
        }


        binding.btnSubmit.setOnClickListener {
            val ratingsdatabase = universal.firebaseDatabase
            val map: MutableMap<String, String> = HashMap()
            map["Place"] = binding.Titletext.text.toString()
            map["Rating"] = binding.ratingbar.rating.toString()
            map["Comments"] = binding.editTextTextMultiLine.text.toString()
            ratingsdatabase.reference.child(auth.uid.toString()).setValue(map)
            updateratings()
        }
    }

    private fun updateratings() {
        val database = Globals().firebaseDatabase
        val firestore = Globals().db
        val ref = database.reference
        ref.addValueEventListener(object : ValueEventListener {
            var counter = 0.0
            var sum = 0.0
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if (ds.child("Place").value.toString() == binding.Titletext.text.toString()) {
                        Log.i("minecraft","+1 rating")
                           counter+=1
                            sum += ds.child("Rating").value.toString().toDouble()
                }
            }
                val newrating = sum/counter
                firestore.collection("Establishments").whereEqualTo("Name",binding.Titletext.text).get().addOnSuccessListener { result ->
                    for(document in result)
                    {
                        Log.i("minecraft", binding.Titletext.text.toString())
                        Log.i("minecraft", document.id)
                        Log.i("minecraft", newrating.toString())
                        Log.i("minecraft","adding new rating")
                        firestore.collection("Establishments").document(document.id).update("Rating",newrating.toString())
                        Toast.makeText(applicationContext,"Rating added!",Toast.LENGTH_LONG).show()
                        val homepageIntent = Intent(applicationContext, Homepage::class.java)
                        startActivity(homepageIntent)
                    }
                }
        }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}

