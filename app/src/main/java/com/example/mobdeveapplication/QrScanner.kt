package com.example.mobdeveapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.example.mobdeveapplication.databinding.ScannerQrBinding

//import kotlinx.android.synthetic.main.scanner_qr.*

private const val CAMERA_CODE = 101
private lateinit var binding: ScannerQrBinding
class QrScanner : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScannerQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.menu.setGroupCheckable(0,false,true)
        binding.bottomNavigationView.setOnItemSelectedListener{ menu ->
            when (menu.itemId) {
                R.id.homenavbar -> {
                    val intent1 = Intent(this, Homepage::class.java)
                    startActivity(intent1)
                    true
                }
                R.id.historynavbar -> {
                    val intent2 = Intent(this, History::class.java)
                    startActivity(intent2)
                    true
                }
                R.id.qrnavbar -> {
                    val intent3 = Intent(this, QrScanner::class.java)
                    startActivity(intent3)
                    true
                }
                R.id.profilenavbar -> {
                    val intent4 = Intent(this, Profile::class.java)
                    startActivity(intent4)
                    true
                }
                R.id.settingsnavbar -> {
                    val intent5 = Intent(this, Settings::class.java)
                    startActivity(intent5)
                    true
                }
                else -> {throw IllegalStateException("something bad happened")}
            }
        }

        setupPermissions()
        codeScanner()
    }
    private fun codeScanner()
    {
        codeScanner = CodeScanner(this,binding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS

            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    if(useRegex(it.text)) {
                        binding.ScanText.text = it.text
                        val intentforrate = Intent(this@QrScanner, RatingScreen::class.java)
                        intentforrate.putExtra("establishment", it.text)
                        startActivity(intentforrate)
                    }
                    else
                        binding.ScanText.text = "Not a valid QR"
                    //binding.ScanText.text = it.text
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

    fun useRegex(input: String): Boolean {
        val regex = Regex(pattern = "^[a-zA-Z0-9_.-]*\$", options = setOf(RegexOption.IGNORE_CASE))
        return input.length == 20 && regex.matches(input)
    }

    override fun onPause()
    {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions()
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