package com.example.mobdeveapplication
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.HomepageBinding
import com.example.mobdeveapplication.datasets.*
import com.google.firebase.auth.FirebaseAuth


private lateinit var binding: HomepageBinding

class Homepage : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private lateinit var searchAdapter: SearchAdapter

    @SuppressLint("SetTextI18n")
    private lateinit var Featuredadapter: Featuredadapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Globals().auth
        binding = HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
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


        readhome(object : Homepagecallback {
            override fun returnvalueplx(value: ArrayList<listingobject>) {
                Featuredadapter = Featuredadapter(applicationContext, value)
                binding.featuredcarousel.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.featuredcarousel.adapter = Featuredadapter
            }
        },"Featured")
        readhome(object : Homepagecallback {
            override fun returnvalueplx(value: ArrayList<listingobject>) {
                Featuredadapter = Featuredadapter(applicationContext, value)
                binding.popularcarousel.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.popularcarousel.adapter = Featuredadapter
            }
        },"Popular")

        binding.btnCategories.setOnClickListener {
            val intentcategories = Intent(this, Filterscategories::class.java)
            startActivity(intentcategories)
        }
        binding.btnNearme.setOnClickListener {
            val intentnearme = Intent(this, Filtersnearme::class.java)
            startActivity(intentnearme)
        }

    }
    interface Homepagecallback {
        fun returnvalueplx(value: ArrayList<listingobject>){
        }
    }
    private fun readhome(homecallback : Homepagecallback, filter: String) {
        val universal = Globals()
        val database = universal.db
        database.collection("Establishments").whereEqualTo(filter, "True").get()
            .addOnSuccessListener { result ->
                val featuredlist = ArrayList<listingobject>()
                for (document in result) {
                    val list = listingobject(document.data["Name"].toString(), Integer.parseInt(document.data["Rating"] as String), document.data["link"].toString())
                    featuredlist.add(list)
                }
                homecallback.returnvalueplx(featuredlist)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        val itemz = menu?.findItem(R.id.cloud_search)
        val searchview = itemz?.actionView as SearchView
        searchview.setOnCloseListener {
            binding.searchResults.visibility = View.INVISIBLE
            false
        }
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchResults.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
                binding.searchResults.adapter = searchAdapter
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val db = Globals().db
                binding.searchResults.visibility = View.VISIBLE
                val arrayrecycler  = ArrayList<searchobject>()
                db.collection("Establishments").whereEqualTo("Name",newText!!).get().addOnSuccessListener{ documents ->
                    for (document in documents)
                    {
                        arrayrecycler.add(searchobject(document.data["Name"].toString()))
                    }
                }.addOnFailureListener{
                    // do something with e (aka error)
                    }
                searchAdapter = SearchAdapter(applicationContext,arrayrecycler)
                binding.searchResults.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
                binding.searchResults.adapter = searchAdapter
                searchAdapter.setOnItemClickListener(object : SearchAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val searchresultintent = Intent(applicationContext, Establishment::class.java)
                        searchresultintent.putExtra("name",arrayrecycler[position].name)
                        startActivity(searchresultintent)
                    }

                })
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
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

