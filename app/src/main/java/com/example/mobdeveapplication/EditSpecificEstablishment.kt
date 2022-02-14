package com.example.mobdeveapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobdeveapplication.databinding.ActivityEditSpecificEstablishmentBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.firestore.SetOptions

private lateinit var binding: ActivityEditSpecificEstablishmentBinding

class EditSpecificEstablishment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSpecificEstablishmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.menu.setGroupCheckable(0, false, true)
        binding.bottomNavigationView.setOnItemSelectedListener { menu ->
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
                else -> {
                    throw IllegalStateException("something bad happened")
                }
            }
        }
        val name = intent.getStringExtra("Name")

        val db = Globals().db
        var Name = ""
        var Rating = ""
        var Longitude = ""
        var Latitude = ""
        var Location = ""
        var link = ""
        var Aircon = ""
        var AirDryer = ""
        var Bidet = ""
        var PowerFlush = ""
        var About = ""
        var Featured = ""
        var Popular = ""
        var Owner = ""

        db.collection("Establishments").whereEqualTo("Name", name).get().addOnSuccessListener {


            for (document in it) {
                Name = document.data.getValue("Name").toString()
                Rating = document.data.getValue("Rating").toString()
                Longitude = document.data.getValue("Longitude").toString()
                Latitude = document.data.getValue("Latitude").toString()
                Location = document.data.getValue("Location").toString()
                link = document.data.getValue("link").toString()
                Aircon = document.data.getValue("Aircon").toString()
                AirDryer = document.data.getValue("AirDryer").toString()
                Bidet = document.data.getValue("Bidet").toString()
                PowerFlush = document.data.getValue("PowerFlush").toString()
                About = document.data.getValue("About").toString()
                Featured = document.data.getValue("Featured").toString()
                Popular = document.data.getValue("Popular").toString()
                Owner = document.data.getValue("Owner").toString()
            }
            binding.Establishmentname.setText(Name)
            binding.Establishmentrating.setText(Rating)
            binding.Establishmentlongitude.setText(Longitude)
            binding.Establishmentlatitude.setText(Latitude)
            binding.Establishmentlocation.setText(Location)
            binding.Establishmentpicture.setText(link)
            if (Aircon == "Yes")
                binding.swAircon.isChecked = true
            if (AirDryer == "Yes")
                binding.swAirdryer.isChecked = true
            if (Bidet == "Yes")
                binding.swBidet.isChecked = true
            if (PowerFlush == "Yes")
                binding.swPowerflush.isChecked = true
            binding.Establishmentabout.setText(About)
        }

        binding.saveChanges.setOnClickListener {

                val establishment: MutableMap<String, Any> = HashMap()
                establishment["About"] = binding.Establishmentabout.text.toString()
                establishment["Featured"] = Featured
                establishment["Latitude"] = binding.Establishmentlatitude.text.toString()
                establishment["Location"] = binding.Establishmentlocation.text.toString()
                establishment["Longitude"] = binding.Establishmentlocation.text.toString()
                establishment["Name"] = binding.Establishmentname.text.toString()
                establishment["Owner"] = Owner
                establishment["Popular"] = Popular
                establishment["Rating"] = binding.Establishmentrating.text.toString()
                establishment["link"] = binding.Establishmentpicture.text.toString()

                if (binding.swAircon.isChecked)
                    establishment["Aircon"] = "Yes"
                else
                    establishment["Aircon"] = "No"
                if (binding.swAirdryer.isChecked)
                    establishment["AirDryer"] = "Yes"
                else
                    establishment["AirDryer"] = "No"
                if (binding.swBidet.isChecked)
                    establishment["Bidet"] = "Yes"
                else
                    establishment["Bidet"] ="No"
                if (binding.swPowerflush.isChecked)
                    establishment["PowerFlush"] = "Yes"
                else
                    establishment["PowerFlush"] = "No"


                    db.collection("Establishments").whereEqualTo("Name", name).get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                db.collection("Establishments").document(document.id).set(establishment, SetOptions.merge())
                            }
                            Toast.makeText(applicationContext, "The establishment is now updated", Toast.LENGTH_SHORT).show()
                            val editEstablishmentIntent = Intent(this, EditEstablishment::class.java)
                            startActivity(editEstablishmentIntent)
                        }
                }
        }
    }