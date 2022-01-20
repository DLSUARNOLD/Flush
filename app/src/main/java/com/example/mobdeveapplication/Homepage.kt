package com.example.mobdeveapplication
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobdeveapplication.databinding.HomepageBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseAuth
import com.example.mobdeveapplication.datasets.*


private lateinit var binding: HomepageBinding

lateinit var mapFragment: SupportMapFragment
lateinit var googleMap : GoogleMap
class Homepage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var Adapter: SearchAdapter

    @SuppressLint("SetTextI18n")
    private lateinit var featuredadapter: featuredadapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Globals().auth
        binding = HomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.profileBtn.setOnClickListener {
            val profileintent = Intent(this, Profile::class.java)
            startActivity(profileintent)
        }
        binding.historyBtn.setOnClickListener {
            val historyintent = Intent(this, History::class.java)
            startActivity(historyintent)
        }
        binding.qrBtn.setOnClickListener {
            val qrintent = Intent(this, QrScanner::class.java)
            startActivity(qrintent)
        }
        binding.estab.setOnClickListener {
            val estab = Intent(this, Establishment::class.java)
            startActivity(estab)
        }
        binding.button1.setOnClickListener {
            auth.signOut()
            val signout = Intent(this, Registerform::class.java)
            startActivity(signout)
        }


        readhome(object : homepagecallback {
            override fun returnvalueplx(value: ArrayList<listingobject>) {
                featuredadapter = featuredadapter(applicationContext, value)
                binding.featuredcarousel.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.featuredcarousel.adapter = featuredadapter
            }
        },"Featured")
        readhome(object : homepagecallback {
            override fun returnvalueplx(value: ArrayList<listingobject>) {
                featuredadapter = featuredadapter(applicationContext, value)
                binding.popularcarousel.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                binding.popularcarousel.adapter = featuredadapter
            }
        },"Popular")
        /*
        mapFragment = supportFragmentManager.findFragmentById(binding.Map.id) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 69420)
            }
                googleMap.isMyLocationEnabled = true

            val sydney = LatLng(-33.852, 151.211)
            googleMap.addMarker(
                MarkerOptions().position(sydney).title("Marker in Sydney")
            )
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15F))
            return@OnMapReadyCallback
        })*/
    }
    interface homepagecallback {
        fun returnvalueplx(value: ArrayList<listingobject>){
        }
    }
    fun readhome(homecallback : homepagecallback,filter: String) {
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
                binding.searchResults.adapter = Adapter
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val db = Globals().db
                binding.searchResults.visibility = View.VISIBLE
                val arrayrecycler  = ArrayList<searchobject>()
                db.collection("Establishments").whereEqualTo("Name",newText!!).get().addOnSuccessListener{ documents ->
                    Log.i("panalo", documents.size().toString())
                    for (document in documents)
                    {
                        arrayrecycler.add(searchobject(document.data["Name"].toString()))
                        Log.i("panalo",document.data["Name"].toString())
                    }
                }.addOnFailureListener{ e ->
                        // do something with e (aka error)
                    }
                Adapter = SearchAdapter(applicationContext,arrayrecycler)
                binding.searchResults.layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
                binding.searchResults.adapter = Adapter
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
    /*fun searchData(input : String){
        val db = Globals().db
        db.collection("Establishments").whereEqualTo("Name",input).get().addOnCompleteListener { document ->
            Toast.makeText(applicationContext, "value is $document", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "failure result", Toast.LENGTH_LONG).show()
        }
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_settings){
            Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
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

