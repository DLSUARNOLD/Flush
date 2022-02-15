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
import com.example.mobdeveapplication.databinding.ActivityHomepageBinding
import com.example.mobdeveapplication.datasets.*
import com.google.firebase.auth.FirebaseAuth


private lateinit var binding: ActivityHomepageBinding

class Homepage : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private lateinit var searchAdapter: SearchAdapter

    @SuppressLint("SetTextI18n")
    private lateinit var Featuredadapter: Featuredadapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Globals().auth
        binding = ActivityHomepageBinding.inflate(layoutInflater)
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


        readhome(object : Homepagecallback {
            override fun returnvalue(value: ArrayList<Listingobject>) {
                Featuredadapter = Featuredadapter(applicationContext, value)
                binding.featuredRecycler.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.featuredRecycler.adapter = Featuredadapter
            }
        },"Featured")
        readhome(object : Homepagecallback {
            override fun returnvalue(value: ArrayList<Listingobject>) {
                Featuredadapter = Featuredadapter(applicationContext, value)
                binding.popularRecycler.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.popularRecycler.adapter = Featuredadapter
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
        fun returnvalue(value: ArrayList<Listingobject>){
        }
    }
    private fun readhome(homecallback : Homepagecallback, filter: String) {
        val universal = Globals()
        val database = universal.db
        database.collection("Establishments").whereEqualTo(filter, "True").get()
            .addOnSuccessListener { result ->
                val featuredlist = ArrayList<Listingobject>()
                for (document in result) {
                    val list = Listingobject(document.data["Name"].toString(),document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString())
                    featuredlist.add(list)
                }
                homecallback.returnvalue(featuredlist)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        val item = menu?.findItem(R.id.cloud_search)
        val searchview = item?.actionView as SearchView
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
                val arrayrecycler  = ArrayList<Searchobject>()
                db.collection("Establishments").whereEqualTo("Name",newText!!).get().addOnSuccessListener{ documents ->
                    for (document in documents)
                    {
                        arrayrecycler.add(Searchobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString()))
                    }
                }
                searchAdapter = SearchAdapter(applicationContext,arrayrecycler)
                binding.searchResults.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
                binding.searchResults.adapter = searchAdapter
                searchAdapter.setOnItemClickListener(object : SearchAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val searchresultintent = Intent(applicationContext, Establishment::class.java)
                        searchresultintent.putExtra("name",arrayrecycler[position].name)
                        searchresultintent.putExtra("rating", arrayrecycler[position].rating)
                        searchresultintent.putExtra("description",arrayrecycler[position].description)
                        searchresultintent.putExtra("picture", arrayrecycler[position].picture)
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

