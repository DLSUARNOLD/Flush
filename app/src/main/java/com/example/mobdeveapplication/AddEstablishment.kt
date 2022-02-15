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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobdeveapplication.databinding.ActivityAddEstablishmentBinding
import com.example.mobdeveapplication.datasets.Globals

private lateinit var binding: ActivityAddEstablishmentBinding

/**
 * represents the activity page in which an privileged user can input and submit his own establishment to the database
 */
class AddEstablishment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEstablishmentBinding.inflate(layoutInflater)
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
                else -> {throw IllegalStateException("Error")}
            }
        }
        binding.submitEstablishment.setOnClickListener {
            val db = Globals().db
            db.collection("Establishments")
                .whereEqualTo("Name", binding.etEstablishmentname.text.toString())
                .get().addOnCompleteListener { result ->
                    if (result.result.isEmpty) {
                        val auth = Globals().auth
                        val name = binding.etEstablishmentname.text.toString()
                        val longitude = binding.etEstablishmentname.text.toString()
                        val latitude = binding.etEstablishmentname.text.toString()
                        val location = binding.etEstablishmentname.text.toString()
                        val picture = binding.etEstablishmentname.text.toString()
                        val about = binding.etEstablishmentname.text.toString()
                        val owner = auth.currentUser?.email.toString()
                        val featured = "False"
                        val popular = "False"
                        var aircon = "No"
                        var bidet = "No"
                        var dryer = "No"
                        var flush = "No"
                        val rating = "0"
                        if (binding.swAircon.isChecked)
                            aircon = "Yes"
                        if (binding.swAirdryer.isChecked)
                            dryer = "Yes"
                        if (binding.swBidet.isChecked)
                            bidet = "Yes"
                        if (binding.swPowerflush.isChecked)
                            flush = "Yes"
                        if (name.isEmpty() || longitude.isEmpty() || latitude.isEmpty() || location.isEmpty() || picture.isEmpty() || about.isEmpty())
                            Toast.makeText(
                                applicationContext,
                                "Please fill up all fields.",
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                        {
                            saveEstablishment(name,rating,longitude,latitude,location,picture,about,owner,featured,popular,aircon,bidet,dryer,flush)
                            val settingIntent = Intent(this, Settings::class.java)
                            startActivity(settingIntent)
                        }
                    } else
                        Toast.makeText(this, "This Establishment already exists.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveEstablishment(name: String, rating: String, longitude: String, latitude: String, location: String, picture: String, about: String, owner: String, featured: String, popular: String, aircon: String, bidet: String, dryer: String, flush: String)
    {
            val db = Globals().db
            val establishment: MutableMap<String, Any> = HashMap()
            establishment["About"] = about
            establishment["AirDryer"] = dryer
            establishment["Aircon"] = aircon
            establishment["Bidet"] = bidet
            establishment["Featured"] = featured
            establishment["Latitude"] = latitude
            establishment["Location"] = location
            establishment["Longitude"] = longitude
            establishment["Name"] = name
            establishment["Owner"] = owner
            establishment["Popular"] = popular
            establishment["PowerFlush"] = flush
            establishment["Rating"] = rating
            establishment["link"] = picture

            db.collection("Establishments")
                .add(establishment)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Establishment added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Establishment failed to add", Toast.LENGTH_SHORT).show()
                }

            val settingIntent = Intent(this, Settings::class.java)
            startActivity(settingIntent)
    }
}