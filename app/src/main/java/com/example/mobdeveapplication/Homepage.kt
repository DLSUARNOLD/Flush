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
/**
 * @author Quiros, Arnold Luigi G.
 * @author Ty, Sam Allyson O.
 *
 * MOBDEVE S11
 * 16/02/2022
 * Version 1.0
 */

private lateinit var binding: ActivityHomepageBinding
/**
 * Represents the homepage of the app
 */
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
                binding.rvFeaturedList.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.rvFeaturedList.adapter = Featuredadapter
            }
        },"Featured")
        readhome(object : Homepagecallback {
            override fun returnvalue(value: ArrayList<Listingobject>) {
                Featuredadapter = Featuredadapter(applicationContext, value)
                binding.rvPopularList.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.rvPopularList.adapter = Featuredadapter
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

    /**
     * callback interface for returning an arraylist for the selected recycler view to use
     */
    interface Homepagecallback {
        fun returnvalue(value: ArrayList<Listingobject>){
        }
    }

    /**
     *  this function evaluates all documents in the firestore database to collect all establishments
     *  that match the given filter. It is then put into an arraylist and is sent to the callback for the recycler view to use
     *
     *  @param homecallback - callback interface to be used
     *  @param filter - filter word to be compared to
     */
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
            binding.rvResults.visibility = View.INVISIBLE
            false
        }
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.rvResults.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
                binding.rvResults.adapter = searchAdapter
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val db = Globals().db
                binding.rvResults.visibility = View.VISIBLE
                val arrayrecycler  = ArrayList<Searchobject>()
                db.collection("Establishments").whereEqualTo("Name",newText!!).get().addOnSuccessListener{ documents ->
                    for (document in documents)
                    {
                        arrayrecycler.add(Searchobject(document.data["Name"].toString(), document.data["Rating"].toString().toDouble(), document.data["link"].toString(),document.data["About"].toString()))
                    }
                }
                searchAdapter = SearchAdapter(applicationContext,arrayrecycler)
                binding.rvResults.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
                binding.rvResults.adapter = searchAdapter
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

