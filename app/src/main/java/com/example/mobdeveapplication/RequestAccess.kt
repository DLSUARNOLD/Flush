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
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobdeveapplication.databinding.ActivityRequestAccessBinding

private lateinit var binding: ActivityRequestAccessBinding

/**
 * Represents the activity screen for requesting elevated privileges that are needed for users to post their own establishments onto the app
 */
class RequestAccess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestAccessBinding.inflate(layoutInflater)
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

        binding.btnSendEmail.setOnClickListener { // allows the current user to send an email to the application in order to ask for admin access
            val email = "FlushApplication@gmail.com"
            val subject = binding.etEditSubject.text.toString()
            val message = binding.etEditMessage.text.toString()

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