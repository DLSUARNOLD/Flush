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
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.mobdeveapplication.databinding.ActivityScannerQrBinding

//import kotlinx.android.synthetic.main.scanner_qr.*

private const val CAMERA_CODE = 101
private lateinit var binding: ActivityScannerQrBinding

/**
 * Represents the activity screen of scanning the QR codes that are used to Redirect the user in order to rate an establishment
 */
class QrScanner : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerQrBinding.inflate(layoutInflater)
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

        setupPermissions()
        codeScanner()
    }
    private fun codeScanner() // declares all of the permissions and modes that will be used by the qr scanner
    {
        codeScanner = CodeScanner(this,binding.csvScanner)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK // access the back camera
            formats = CodeScanner.ALL_FORMATS // scans all formats qr code, bar code, etc.

            autoFocusMode = AutoFocusMode.SAFE // autofocuses on the qr
            scanMode = ScanMode.CONTINUOUS // continuous scanning so the user does not need to click to scan

            isAutoFocusEnabled = true
            isFlashEnabled = false

                //scans the qr code and redirects it to the establishment that was scanned for rating
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    if(useRegex(it.text)) {
                        binding.tvScantext.text = it.text
                        val intentforrate = Intent(this@QrScanner, RatingScreen::class.java)
                        intentforrate.putExtra("establishment", it.text)
                        startActivity(intentforrate)
                    }
                    else
                        binding.tvScantext.text = it.text
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("QR", "Camera initialization error: ${it.message}")
                }
            }
        }
    }

    override fun onResume()
    {
        super.onResume()
        codeScanner.startPreview()
    }

    private fun useRegex(input: String): Boolean {
        val regex = Regex(pattern = "^[a-zA-Z0-9_.-]*\$", options = setOf(RegexOption.IGNORE_CASE))
        return input.length == 20 && regex.matches(input)
    }

    override fun onPause()
    {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() // asks for permission if the camera is not granted
    {
        val permission:Int = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest()
    {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "You need to grant the application camera access!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}