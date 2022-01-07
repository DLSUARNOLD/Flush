package com.example.mobdeveapplication
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.HistoryBinding
import com.example.mobdeveapplication.datasets.Adapter
import com.example.mobdeveapplication.datasets.Globals
import com.example.mobdeveapplication.datasets.Historyobject
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.ArrayList
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener











private lateinit var binding: HistoryBinding
private lateinit var auth: FirebaseAuth
//private var historylist  = ArrayList<Historyobject>()
class History : AppCompatActivity() {
    private lateinit var Adapter : Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        binding = HistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readData(object : Callbacker {
            override fun returnvaluepls(value: ArrayList<Historyobject>) {
                Adapter = Adapter(applicationContext, value)
                binding.recycler.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
                binding.recycler.adapter = Adapter
            }
        })
    }
    interface Callbacker {
        fun returnvaluepls(value: ArrayList<Historyobject>){
        }
    }
    fun readData(callbackobject : Callbacker) {
        val universal = Globals()
        val database = universal.firebaseDatabase
        val ref = database.getReference("User").child(auth.currentUser!!.uid)
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                var templist  = ArrayList<Historyobject>()
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                         val list = Historyobject(ds.key.toString(), ds.getValue(Int::class.java)!!)
                         templist.add(list)

                    }
                    callbackobject.returnvaluepls(templist)
                }
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

}
