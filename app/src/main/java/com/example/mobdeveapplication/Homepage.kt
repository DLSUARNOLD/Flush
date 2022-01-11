package com.example.mobdeveapplication
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobdeveapplication.databinding.HomepageBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.concurrent.timerTask


private lateinit var binding: HomepageBinding
class Homepage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap : GoogleMap
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val universal = Globals()
        auth = universal.auth
        binding = HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewWelcome.text = "Welcome ${auth.currentUser?.displayName}"
        binding.profileBtn.setOnClickListener {
            val profileintent = Intent(this, Profile::class.java)
            startActivity(profileintent)
        }
        binding.historyBtn.setOnClickListener {
            val historyintent = Intent(this, History::class.java)
            startActivity(historyintent)
        }
        binding.qrBtn.setOnClickListener {
            val qrintent = Intent(this, QrScanner::class.java)
            startActivity(qrintent)
        }
        binding.estab.setOnClickListener {
            val estab = Intent(this, Establishment::class.java)
            startActivity(estab)
        }
        mapFragment = supportFragmentManager.findFragmentById(binding.Map.id) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 69420)
            }
                googleMap.isMyLocationEnabled = true

            val sydney = LatLng(-33.852, 151.211)
            googleMap.addMarker(
                MarkerOptions().position(sydney).title("Marker in Sydney")
            )
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15F))
            return@OnMapReadyCallback
        })
    }
    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser !=null){
            val intent = Intent(this, Homepage::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 69420){
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "You need to grant the application location access!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
