package com.example.mobdeveapplication
/**
 * @author Quiros, Arnold Luigi G.
 * @author Ty, Sam Allyson O.
 *
 * MOBDEVE S11
 * 16/02/2022
 * Version 1.0
 */

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.ActivityFilterscategoriesBinding
import com.example.mobdeveapplication.datasets.Filteradapter
import com.example.mobdeveapplication.datasets.Globals
import com.example.mobdeveapplication.datasets.Listingobject
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.collections.ArrayList


private lateinit var binding: ActivityFilterscategoriesBinding


/**
 * Represents the activity screen in which the user can toggle categories and be shown a list of establishments that conform to the filter.
 */
class Filterscategories : AppCompatActivity() {
    private lateinit var filteradapter: Filteradapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterscategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updatereadcategories(object : Filtercategories {
            override fun returnvalue(value: ArrayList<Listingobject>) {
                filteradapter = Filteradapter(applicationContext, value)
                binding.rvFilterresults.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                binding.rvFilterresults.adapter = filteradapter
            }
        })

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
    }
}
interface Filtercategories {
    fun returnvalue(value: ArrayList<Listingobject>){
    }
}
private fun updatereadcategories(homecallback : Filtercategories) {
        binding.swBidet.setOnCheckedChangeListener { _, _ ->

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
    if(binding.swBidet.isChecked && binding.swAircon.isChecked && binding.swAirdryer.isChecked && binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").whereEqualTo("Aircon","Yes").whereEqualTo("AirDryer","Yes").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(),document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swBidet.isChecked && binding.swAircon.isChecked && binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").whereEqualTo("Bidet","Yes").whereEqualTo("AirDryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swAircon.isChecked && binding.swAirdryer.isChecked && binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").whereEqualTo("AirDryer","Yes").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swBidet.isChecked && binding.swAircon.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").whereEqualTo("Aircon","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swBidet.isChecked && binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swBidet.isChecked && binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").whereEqualTo("AirDryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swAircon.isChecked && binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").whereEqualTo("AirDryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swAircon.isChecked && binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swPowerflush.isChecked && binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("PowerFlush","Yes").whereEqualTo("AirDryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swAirdryer.isChecked){
        database.collection("Establishments").whereEqualTo("AirDryer","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swBidet.isChecked){
        database.collection("Establishments").whereEqualTo("Bidet","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swPowerflush.isChecked){
        database.collection("Establishments").whereEqualTo("PowerFlush","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else if(binding.swAircon.isChecked){
        database.collection("Establishments").whereEqualTo("Aircon","Yes").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }
    else{
        database.collection("Establishments").get().addOnSuccessListener { result ->
            val filterlist = ArrayList<Listingobject>()
            for (document in result) {
                val list = Listingobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                filterlist.add(list)
            }
            homecallback.returnvalue(filterlist)
        }
    }


}
