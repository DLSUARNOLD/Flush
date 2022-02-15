package com.example.mobdeveapplication
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

class Profile : AppCompatActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val universal = Globals()
        auth = universal.auth
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
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
                    binding.textName.setText(auth.currentUser?.displayName)
                    binding.Greetingbox.text = "Hello ${auth.currentUser?.email}"
        binding.saveName.setOnClickListener {
            val profileupdate = userProfileChangeRequest {
                displayName = binding.textName.text.toString()
            }
            auth.currentUser?.updateProfile(profileupdate)
            Toast.makeText(this,"Name change has been saved",Toast.LENGTH_LONG).show()
        }
        binding.savepassword.setOnClickListener {
            auth.sendPasswordResetEmail(auth.currentUser?.email.toString())
            Toast.makeText(this,"An email has been sent to your email", Toast.LENGTH_LONG).show()
        }

        binding.deleteAccount.setOnClickListener {
            CoroutineScope(IO).launch{
                val job1 = async{deleteuserinfo()}
                val job2 = async{logoutanddelete()}
                job1.await()
                job2.await()
            }
        }
        Picasso.get().load(auth.currentUser?.photoUrl).fit().into(binding.profilepicture)
    }
     private suspend fun deleteuserinfo(){
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
     private suspend fun logoutanddelete(){
         delay(1700)
        auth.currentUser?.delete()
        auth.signOut()
        val signedout = Intent(this, Registerform::class.java)
        startActivity(signedout)
    }

}
