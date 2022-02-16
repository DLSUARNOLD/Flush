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
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobdeveapplication.databinding.ActivityProfileBinding
import com.example.mobdeveapplication.datasets.Globals
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.coroutines.*


private lateinit var binding: ActivityProfileBinding
private lateinit var auth: FirebaseAuth

/**
 * Represents the profile page of the app in which they can change their name, change their password, or even delete their account
 */
class Profile : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.menu.setGroupCheckable(0,false,true) // displays the bottom navigation
        binding.bottomNavigationView.setOnItemSelectedListener{ menu -> // redirects user to a new page depending on what button is pressed
            when (menu.itemId) {
                R.id.homenavbar -> { // redirects user to homepage
                    val homepageIntent = Intent(this, Homepage::class.java)
                    startActivity(homepageIntent)
                    true
                }
                R.id.historynavbar -> { // redirects user to history page
                    val historyIntent = Intent(this, History::class.java)
                    startActivity(historyIntent)
                    true
                }
                R.id.qrnavbar -> { // redirects user to qr scanner page
                    val qrIntent = Intent(this, QrScanner::class.java)
                    startActivity(qrIntent)
                    true
                }
                R.id.profilenavbar -> { // redirects user to profile page
                    val profileIntent = Intent(this, Profile::class.java)
                    startActivity(profileIntent)
                    true
                }
                R.id.settingsnavbar -> { // redirects user to settings page
                    val settingIntent = Intent(this, Settings::class.java)
                    startActivity(settingIntent)
                    true
                }
                else -> {throw IllegalStateException("Error")}
            }
        }
                    binding.etTextName.setText(auth.currentUser?.displayName)
                    binding.tvGreeting.text = "Hello ${auth.currentUser?.email}"
        binding.btnSaveName.setOnClickListener {
            val profileupdate = userProfileChangeRequest {
                displayName = binding.etTextName.text.toString()
            }
            auth.currentUser?.updateProfile(profileupdate)
            Toast.makeText(this,"Name change has been saved",Toast.LENGTH_LONG).show()
        }
        binding.btnSavepassword.setOnClickListener {
            auth.sendPasswordResetEmail(auth.currentUser?.email.toString())
            Toast.makeText(this,"An email has been sent to your email", Toast.LENGTH_LONG).show()
        }

        binding.btnDeleteAccount.setOnClickListener {
            CoroutineScope(IO).launch{
                val job1 = async{deleteuserinfo()}
                val job2 = async{logoutanddelete()}
                job1.await()
                job2.await()
            }
        }
        Picasso.get().load(auth.currentUser?.photoUrl).fit().into(binding.ivProfilepicture)
    }
     private suspend fun deleteuserinfo(){ // deletes all the establishments owned by the user (if any) from the database
         delay(100)
        val db = Globals().db
        db.collection("Establishments").whereEqualTo("Owner", auth.currentUser?.email).get().addOnSuccessListener {
            Toast.makeText(applicationContext, auth.currentUser?.email.toString(), Toast.LENGTH_SHORT).show()
            for (document in it)
                db.collection("Establishments").document(document.id).delete()
        }

        db.collection("AdminAccess").whereEqualTo("email", auth.currentUser?.email).get()
            .addOnSuccessListener { result ->
                Toast.makeText(applicationContext, auth.currentUser?.email.toString(), Toast.LENGTH_SHORT).show()
                for (document in result)
                {
                    db.collection("AdminAccess").document(document.id).delete()
                }
            }
    }
     private suspend fun logoutanddelete(){ // deletes the user account from the database and signs out
         delay(1700)
        auth.currentUser?.delete()
        auth.signOut()
        val signedout = Intent(this, Registerform::class.java)
        startActivity(signedout)
    }

}
