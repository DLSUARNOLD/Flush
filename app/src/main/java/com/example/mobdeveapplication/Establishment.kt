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
import android.util.Log
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


private lateinit var binding: ActivityEstablishmentBinding
private lateinit var auth: FirebaseAuth
private lateinit var mMap: GoogleMap

/**
 * Represents the activity screen in which the user can view all important information about a specific listing
 */
class Establishment : AppCompatActivity(), OnMapReadyCallback {
    @OptIn(DelicateCoroutinesApi::class)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        val firestore = universal.db
        super.onCreate(savedInstanceState)
        binding = ActivityEstablishmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment =
            supportFragmentManager.findFragmentById(binding.Map.id) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.bottomNavigationView.menu.setGroupCheckable(
            0,
            false,
            true
        ) // displays the bottom navigation
        binding.bottomNavigationView.setOnItemSelectedListener { menu -> // redirects user to a new page depending on what button is pressed
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
                else -> {
                    throw IllegalStateException("Error")
                }
            }
        }



        //sets the value for the displays in the Establishment page
        val intent = intent
        val lat = intent.getStringExtra("latitude")
        val long = intent.getStringExtra("longitude")
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
        GlobalScope.launch(Dispatchers.IO) {
            val response = URL("https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&units=metric&appid=614150d059399dcba5bdf9329aeaa0ec").readText(
                Charsets.UTF_8)
            val json = JSONObject(response)
            val main = json.getJSONObject("main")
            val wind = json.getJSONObject("wind")
            val weather = json.getJSONArray("weather").getJSONObject(0)
            val updatedat: Long = json.getLong("dt")
            val updatedtext = "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedat * 1000))
            val temp = main.getString("temp") + "°C"
            val windspeed = wind.getString("speed")
            val weatherdescription = weather.getString("description")
            binding.tvWeather.text = "Current weather: " + weatherdescription + "\n" + temp + "    " + windspeed + "mph\n" + updatedtext
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val firestore = Globals().db
        firestore.collection("Establishments").whereEqualTo("Name", binding.tvTitle.text).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    mMap = googleMap
                    val location = LatLng(
                        document.data["Latitude"].toString().toDouble(),
                        document.data["Longitude"].toString().toDouble()
                    )
                    mMap.addMarker(MarkerOptions().position(location).title("Restroom here"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                    mMap.setMinZoomPreference(15.0f)
                    mMap.setMaxZoomPreference(18.0f)

                }
            }
    }

}
