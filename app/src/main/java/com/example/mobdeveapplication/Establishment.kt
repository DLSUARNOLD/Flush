package com.example.mobdeveapplication
/**
 * @author Quiros, Arnold Luigi G.
 * @author Ty, Sam Allyson O.
 *
 * MOBDEVE S11
 * 16/02/2022
 * Version 1.0
 */
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.ActivityEstablishmentBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


private lateinit var binding: ActivityEstablishmentBinding
private lateinit var auth: FirebaseAuth
private lateinit var mMap: GoogleMap

/**
 * Represents the activity screen in which the user can view all important information about a specific listing
 */
class Establishment : AppCompatActivity(), OnMapReadyCallback {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        val firestore = universal.db
        super.onCreate(savedInstanceState)
        binding = ActivityEstablishmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentById(binding.Map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
        binding.tvTitle.text = intent.getStringExtra("name")
        binding.tvDescription.text = intent.getStringExtra("description")
        Picasso.get().load(intent.getStringExtra("picture")).fit().into(binding.ivProfile)
        binding.rbHistoryItemRating.rating = intent.getDoubleExtra("rating", 0.0).toFloat()
        var directionslink = ""
        firestore.collection("Establishments").whereEqualTo("Name", binding.tvTitle.text).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    directionslink = document.data["Location"].toString()
                }
                binding.btnDirections.setOnClickListener {
                    val mapsintent = Intent(Intent.ACTION_VIEW, Uri.parse(directionslink))
                    startActivity(mapsintent)
                }
            }

    }
    override fun onMapReady(googleMap: GoogleMap) {
        val firestore = Globals().db
        firestore.collection("Establishments").whereEqualTo("Name", binding.tvTitle.text).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    mMap = googleMap
                    val location = LatLng(document.data["Latitude"].toString().toDouble(),document.data["Longitude"].toString().toDouble())
                    mMap.addMarker(MarkerOptions().position(location).title("Marker in Sydney"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                    mMap.setMinZoomPreference(15.0f)
                    mMap.setMaxZoomPreference(18.0f)
                }
            }
    }
}
