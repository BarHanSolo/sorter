package com.sortingrocks.sorter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.CommonStatusCodes

class MainActivity : AppCompatActivity() {
    lateinit var barcodeResult : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        }

        barcodeResult = findViewById(R.id.barcode_result)
    }

    fun scanBarcode(v: View?) {
        var buttonPressed = v?.context?.resources?.getResourceEntryName(v.id)
        //Log.i("TAG", buttonPressed)
        var intent = Intent(this, ScanBarcodeActivity::class.java)
        if (buttonPressed=="scanButton"){
            intent.putExtra("goal", "scan")
            startActivityForResult(intent, 0)
        } else if (buttonPressed=="buttonAdd"){
            intent.putExtra("goal", "add")
            startActivityForResult(intent, 0)
        } else {
            intent.putExtra("goal", "add")
            startActivityForResult(intent, 0)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==0){
            if (resultCode==CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    var barcode = data.getStringExtra("barcode")
                    barcodeResult.setText(barcode)
                } else {
                    barcodeResult.setText("No barcode found")
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    //menu handling
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_options_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.add -> {
                scanBarcode(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
