package com.example.mobdeveapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.ActivityDeleteEstablishmentBinding
import com.example.mobdeveapplication.datasets.DeleteEstablishmentAdapter
import com.example.mobdeveapplication.datasets.Establishmentobject
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth

private lateinit var binding: ActivityDeleteEstablishmentBinding
private lateinit var auth: FirebaseAuth


class DeleteEstablishment : AppCompatActivity() {
    private lateinit var Establishmentadapter: DeleteEstablishmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteEstablishmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                else -> {throw IllegalStateException("something bad happened")}
            }
        }

        readestablishment(object : Establishmentcallback {
            override fun returnvalue(value: ArrayList<Establishmentobject>) {
                Establishmentadapter = DeleteEstablishmentAdapter(applicationContext, value)
                binding.establishmentrecycler.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                binding.establishmentrecycler.adapter = Establishmentadapter
            }
        })
    }

    interface Establishmentcallback {
        fun returnvalue(value: ArrayList<Establishmentobject>){
        }
    }

    private fun readestablishment(establishmentcallback : Establishmentcallback) {
        val universal = Globals()
        val database = universal.db
        auth = Globals().auth
        val email = auth.currentUser?.email.toString()
        database.collection("Establishments").whereEqualTo("Owner", email).get()
            .addOnSuccessListener { result ->
                val establishmentlist = ArrayList<Establishmentobject>()
                for (document in result) {
                    val list = Establishmentobject(document.data["Name"].toString(), document.data["Owner"].toString())
                    establishmentlist.add(list)
                }
                establishmentcallback.returnvalue(establishmentlist)
            }
    }
}
