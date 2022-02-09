package com.example.mobdeveapplication
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.ActivityRatingScreenBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
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
        db.collection("Establishments").document(intent.getStringExtra("establishment").toString()).get().addOnSuccessListener { documents ->
                binding.Titletext.text = documents.data!!["Name"].toString()
                Picasso.get().load(documents.data!!["link"].toString()).fit().into(binding.imageView)
                binding.tvRatingquestion.text = "How would you rate your experience with " + documents.data!!["Name"].toString()
        }


        binding.btnSubmit.setOnClickListener {
            val ratingsdatabase = universal.firebaseDatabase
            val map: MutableMap<String, String> = HashMap()
            map["Reviewer"] = auth.uid.toString()
            map["Rating"] = binding.ratingbar.rating.toString()
            map["Comments"] = binding.editTextTextMultiLine.text.toString()
            ratingsdatabase.reference.child(binding.Titletext.text.toString()).setValue(map)
        }
    }
}
