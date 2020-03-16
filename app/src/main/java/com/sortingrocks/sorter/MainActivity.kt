package com.sortingrocks.sorter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode

class MainActivity : AppCompatActivity() {
    lateinit var barcodeResult : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barcodeResult = findViewById(R.id.barcode_result)
    }
    fun scanBarcode(v: View) {
        var intent = Intent(this, ScanBarcodeActivity::class.java)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==0){
            if (resultCode==CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    var barcode = data.getParcelableExtra<Barcode>("barcode")
                    barcodeResult.setText(barcode.displayValue)
                } else {
                    barcodeResult.setText("No barcode found")
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
