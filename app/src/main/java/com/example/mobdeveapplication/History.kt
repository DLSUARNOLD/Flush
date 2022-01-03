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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.profile.*


private lateinit var binding: HistoryBinding
private lateinit var auth: FirebaseAuth
private var historylist  = ArrayList<Historyobject>()
class History : AppCompatActivity() {
    private lateinit var Adapter : Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        binding = HistoryBinding.inflate(layoutInflater)
        Adapter = Adapter(applicationContext, historylist)
        setContentView(binding.root)
        binding.recycler.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        binding.recycler.adapter = Adapter
        historylist.add(Historyobject("profile1",1))
        historylist.add(Historyobject("profile2",2))
        historylist.add(Historyobject("profile1",3))
        historylist.add(Historyobject("profile2",4))
        historylist.add(Historyobject("profile1",5))
        historylist.add(Historyobject("profile2",6))




    }
}
