package com.example.mobdeveapplication
/**
 * @author Quiros, Arnold Luigi G.
 * @author Ty, Sam Allyson O.
 *
 * MOBDEVE S11
 * 16/02/2022
 * Version 1.0
 */
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.ActivityRatingScreenBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso



private lateinit var binding: ActivityRatingScreenBinding
private lateinit var auth: FirebaseAuth

/**
 * Represents the activity screen for users to rate a specific establishment
 */
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
            binding.tvTitle.text = documents.data!!["Name"].toString()
            Picasso.get().load(documents.data!!["link"].toString()).fit().into(binding.ivProfile)
            binding.tvRatingquestion.text =
                "How would you rate your experience with " + documents.data!!["Name"].toString()
        }

        binding.bottomNavigationView.menu.setGroupCheckable(0,false,true) // displays the bottom navigation
        binding.bottomNavigationView.setOnItemSelectedListener{ menu -> // redirects user to a new page depending on what button is pressed
            when (menu.itemId) {
                R.id.homenavbar -> { // redirects user to homepage
                    val homepageIntent = Intent(this, Homepage::class.java)
                    startActivity(homepageIntent)
                    true
                }
                R.id.historynavbar -> { // redirects user to history page
                    val historyIntent = Intent(this, History::class.java)
                    startActivity(historyIntent)
                    true
                }
                R.id.qrnavbar -> { // redirects user to qr scanner page
                    val qrIntent = Intent(this, QrScanner::class.java)
                    startActivity(qrIntent)
                    true
                }
                R.id.profilenavbar -> { // redirects user to profile page
                    val profileIntent = Intent(this, Profile::class.java)
                    startActivity(profileIntent)
                    true
                }
                R.id.settingsnavbar -> { // redirects user to settings page
                    val settingIntent = Intent(this, Settings::class.java)
                    startActivity(settingIntent)
                    true
                }
                else -> {throw IllegalStateException("Error")}
            }
        }

        binding.btnSubmit.setOnClickListener {
            val ratingsdatabase = universal.firebaseDatabase
            val map: MutableMap<String, String> = HashMap()
            map["Place"] = binding.tvTitle.text.toString()
            map["Rating"] = binding.rbHistoryItemRating.rating.toString()
            map["Comments"] = binding.etComments.text.toString()
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
                    if (ds.child("Place").value.toString() == binding.tvTitle.text.toString()) {
                        Log.i("minecraft","+1 rating")
                           counter+=1
                            sum += ds.child("Rating").value.toString().toDouble()
                }
            }
                val newrating = sum/counter
                firestore.collection("Establishments").whereEqualTo("Name",binding.tvTitle.text).get().addOnSuccessListener { result ->
                    for(document in result)
                    {
                        Log.i("minecraft", binding.tvTitle.text.toString())
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

