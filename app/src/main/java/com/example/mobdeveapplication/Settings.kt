package com.example.mobdeveapplication
/**
 * @author Quiros, Arnold Luigi G.
 * @author Ty, Sam Allyson O.
 *
 * MOBDEVE S11
 * 16/02/2022
 * Version 1.0
 */
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobdeveapplication.databinding.ActivitySettingsBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth

private lateinit var binding: ActivitySettingsBinding

/**
 * Represents the activity screen in which the can add/edit/delete their own establishments given that they have such privileges in their account
 */
class Settings : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Globals().auth
        val db = Globals().db
        binding = ActivitySettingsBinding.inflate(layoutInflater)
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
        binding.btnAddEstablishment.setOnClickListener {

            val email = auth.currentUser?.email

            db.collection("AdminAccess").whereEqualTo("email", email).get()
                .addOnSuccessListener { documents ->
                    var admin = ""
                    for (document in documents)
                        admin = document.data["access"].toString()

                    if (admin == "Yes")
                    {
                        val addEstablishmentIntent = Intent(this, AddEstablishment::class.java)
                        startActivity(addEstablishmentIntent)
                    }
                    else
                        Toast.makeText(applicationContext, "Please Request Admin Access.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEditEstablishment.setOnClickListener {
            val email = auth.currentUser?.email

            db.collection("AdminAccess").whereEqualTo("email", email).get()
                .addOnSuccessListener { documents ->
                    var admin = ""
                    for (document in documents)
                        admin = document.data["access"].toString()

                    if (admin == "Yes")
                    {
                        val editEstablishmentIntent = Intent(this, EditEstablishment::class.java)
                        startActivity(editEstablishmentIntent)
                    }
                    else
                        Toast.makeText(applicationContext, "Please Request Admin Access.", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnDeleteEstablishment.setOnClickListener{
            val email = auth.currentUser?.email

            db.collection("AdminAccess").whereEqualTo("email", email).get()
                .addOnSuccessListener { documents ->
                    var admin = ""
                    for (document in documents)
                        admin = document.data["access"].toString()

                    if (admin == "Yes")
                    {
                        val editEstablishmentIntent = Intent(this, DeleteEstablishment::class.java)
                        startActivity(editEstablishmentIntent)
                    }
                    else
                        Toast.makeText(applicationContext, "Please Request Admin Access.", Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnRequestAccess.setOnClickListener {

            val email = auth.currentUser?.email

            db.collection("AdminAccess").whereEqualTo("email", email).get()
                .addOnSuccessListener { documents ->
                    var admin = ""
                    for (document in documents)
                        admin = document.data["access"].toString()

                    if (admin == "No")
                    {
                        val requestAccessIntent = Intent(applicationContext, RequestAccess::class.java)
                        startActivity(requestAccessIntent)
                    }
                    else
                        Toast.makeText(
                            applicationContext,
                            "You Already Have Admin Access.",
                            Toast.LENGTH_SHORT
                        ).show()
                }
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val registerIntent = Intent(this, Registerform::class.java)
            startActivity(registerIntent)
        }
    }
}