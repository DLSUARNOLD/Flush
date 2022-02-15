package com.example.mobdeveapplication
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.ActivityHistoryBinding
import com.example.mobdeveapplication.datasets.HistoryAdapter
import com.example.mobdeveapplication.datasets.Globals
import com.example.mobdeveapplication.datasets.Historyobject
import com.facebook.FacebookSdk
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.ArrayList
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener



private lateinit var binding: ActivityHistoryBinding
private lateinit var auth: FirebaseAuth
class History : AppCompatActivity() {
    private lateinit var Adapter : HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.menu.setGroupCheckable(0,false,true)
        readData(object : Callbacker {
            override fun returnvalue(value: ArrayList<Historyobject>) {
                Adapter = HistoryAdapter(applicationContext, value,this@History)
                binding.historyRecycler.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
                binding.historyRecycler.adapter = Adapter
            }
        })
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
    interface Callbacker {
        fun returnvalue(value: ArrayList<Historyobject>){
        }
    }
    fun readData(callbackobject : Callbacker) {
        val universal = Globals()
        val database = universal.firebaseDatabase
        val ref = database.reference
            ref.addValueEventListener(object : ValueEventListener {
                var templist  = ArrayList<Historyobject>()
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        if(ds.key.toString() == auth.uid)
                        {
                            val list =
                                Historyobject(ds.child("Place").value.toString(), ds.child("Rating").value.toString().toFloatOrNull()!!)
                            templist.add(list)
                        }
                    }
                    callbackobject.returnvalue(templist)
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }


}
