package com.example.mobdeveapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobdeveapplication.databinding.ActivityRequestAccessBinding

private lateinit var binding: ActivityRequestAccessBinding

class RequestAccess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestAccessBinding.inflate(layoutInflater)
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

        binding.sendEmail.setOnClickListener {
            val email = "FlushApplication@gmail.com"
            val subject = binding.editSubject.text.toString()
            val message = binding.editMessage.text.toString()

            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
            }

            if (subject.isEmpty() || message.isEmpty())
                Toast.makeText(applicationContext, "Fill out all the fields.", Toast.LENGTH_SHORT).show()
            else
            {
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(emailIntent)
                }
                else
                    Toast.makeText(applicationContext, "Required app is not installed.", Toast.LENGTH_SHORT).show()
            }
        }

    }


}