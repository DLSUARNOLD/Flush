package com.example.mobdeveapplication


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.ActivityFilterscategoriesBinding
import com.example.mobdeveapplication.datasets.Filteradapter
import com.example.mobdeveapplication.datasets.Globals
import com.example.mobdeveapplication.datasets.listingobject
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.collections.ArrayList


private lateinit var binding: ActivityFilterscategoriesBinding



class Filterscategories : AppCompatActivity() {
    private lateinit var filteradapter: Filteradapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterscategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updatereadcategories(object : Filtercategories {
            override fun returnvalueplx(value: ArrayList<listingobject>) {
                filteradapter = Filteradapter(applicationContext, value)
                binding.popularcarousel.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                binding.popularcarousel.adapter = filteradapter
            }
        })

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
}
interface Filtercategories {
    fun returnvalueplx(value: ArrayList<listingobject>){
    }
}
private fun updatereadcategories(homecallback : Filtercategories) {
        binding.Bidet.setOnCheckedChangeListener { _, _ ->

        Updatelist(homecallback)
    }
    binding.swAircon.setOnCheckedChangeListener { _, _ ->
        Updatelist(homecallback)
    }
    binding.swAirdryer.setOnCheckedChangeListener { _, _ ->
        Updatelist(homecallback)
    }
    binding.swPowerflush.setOnCheckedChangeListener { _, _ ->
        Updatelist(homecallback)
    }

}
private fun Updatelist(homecallback : Filtercategories)
{
    val universal = Globals()
    val database = universal.db
    if(binding.Bidet.isChecked && binding.swAircon.isChecked && binding.swAirdryer.isChecked && binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").whereEqualTo("Aircon","Yes").whereEqualTo("AirDryer","Yes").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.Bidet.isChecked && binding.swAircon.isChecked && binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").whereEqualTo("Bidet","Yes").whereEqualTo("AirDryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.swAircon.isChecked && binding.swAirdryer.isChecked && binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").whereEqualTo("AirDryer","Yes").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.Bidet.isChecked && binding.swAircon.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").whereEqualTo("Aircon","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.Bidet.isChecked && binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.Bidet.isChecked && binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").whereEqualTo("AirDryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.swAircon.isChecked && binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").whereEqualTo("AirDryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.swAircon.isChecked && binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.swPowerflush.isChecked && binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("PowerFlush","Yes").whereEqualTo("AirDryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("Airdryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.Bidet.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else if(binding.swAircon.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }
    else{
        database.collection("Establishments").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<listingobject>()
            for (document in result) {
                val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalueplx(filterlist)
        }
    }


}
