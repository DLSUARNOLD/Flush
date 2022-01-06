package com.example.mobdeveapplication
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.HistoryBinding
import com.example.mobdeveapplication.databinding.ProfileBinding
import com.example.mobdeveapplication.datasets.Adapter
import com.example.mobdeveapplication.datasets.Globals
import com.example.mobdeveapplication.datasets.Historyobject
import com.example.mobdeveapplication.datasets.UserC
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.profile.*
import java.util.*
import kotlin.collections.ArrayList
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.FirebaseDatabase





private lateinit var binding: HistoryBinding
private lateinit var auth: FirebaseAuth
private var historylist  = ArrayList<Historyobject>()
class History : AppCompatActivity() {
    private lateinit var Adapter : Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        val database = universal.firebaseDatabase
        super.onCreate(savedInstanceState)
        binding = HistoryBinding.inflate(layoutInflater)
        Adapter = Adapter(applicationContext, historylist)
        setContentView(binding.root)
        binding.recycler.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        binding.recycler.adapter = Adapter
        database.getReference("User").child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val user: Historyobject? = snapshot.getValue(Historyobject::class.java)
                       Log.i("Firebase",user!!.location)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        //val unit = Historyobject("Resorts World Manila", 5)
        //val unit2 = Historyobject("Makati Shangrila", 2)
        //val ref = database.getReference(auth.currentUser!!.uid)
        //ref.child(Calendar.getInstance().time.toString()).setValue(unit)
        //ref.child(Calendar.getInstance().time.toString()).setValue(unit2)





    }
}
