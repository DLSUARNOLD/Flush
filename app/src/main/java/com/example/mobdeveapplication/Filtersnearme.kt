package com.example.mobdeveapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.ActivityFiltersnearmeBinding
import com.example.mobdeveapplication.datasets.Filteradapter
import com.example.mobdeveapplication.datasets.Globals
import com.example.mobdeveapplication.datasets.listingobject
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.collections.ArrayList


private lateinit var binding: ActivityFiltersnearmeBinding



class Filtersnearme : AppCompatActivity() {
    private lateinit var filteradapter: Filteradapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiltersnearmeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        readfilter(object : Filternearinterface {
            override fun returnvalueplx(value: ArrayList<listingobject>) {
                filteradapter = Filteradapter(applicationContext, value)
                binding.popularcarousel.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                binding.popularcarousel.adapter = filteradapter
            }
        },this)

        binding.bottomNavigationView.menu.setGroupCheckable(0,false,true)
        binding.bottomNavigationView.setOnItemSelectedListener{ menu ->
            when (menu.itemId) {
                R.id.homenavbar -> {
                    val intentforhomepage = Intent(this, Homepage::class.java)
                    startActivity(intentforhomepage)
                    true
                }
                R.id.historynavbar -> {
                    val intentforhistory = Intent(this, History::class.java)
                    startActivity(intentforhistory)
                    true
                }
                R.id.qrnavbar -> {
                    val intentforqr = Intent(this, QrScanner::class.java)
                    startActivity(intentforqr)
                    true
                }
                R.id.profilenavbar -> {
                    val intent4 = Intent(this, Profile::class.java)
                    startActivity(intent4)
                    true
                }
                R.id.settingsnavbar -> {
                    val intent5 = Intent(this, Settings::class.java)
                    startActivity(intent5)
                    true
                }
                else -> {throw IllegalStateException("something bad happened")}
            }
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
interface Filternearinterface {
    fun returnvalueplx(value: ArrayList<listingobject>){
    }
}
private fun readfilter(homecallback : Filternearinterface, activity : Activity) {
    val universal = Globals()
    val database = universal.db
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    if (ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION), 69420)
        return
    }
    fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
            database.collection("Establishments").get().addOnSuccessListener { result ->
                val filterlist = ArrayList<listingobject>()
                for (document in result) {
                    val currentloc = LatLng(location!!.latitude,location.longitude)
                    val restroomlocation = LatLng(document.data["Latitude"].toString().toDouble(), document.data["Longitude"].toString().toDouble())
                    if(SphericalUtil.computeDistanceBetween(currentloc,restroomlocation)<=25.0)
                    {
                        val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                        filterlist.add(list)
                    }
                }
                homecallback.returnvalueplx(filterlist)
            }
        }
}
