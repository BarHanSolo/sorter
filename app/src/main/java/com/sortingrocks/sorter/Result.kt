package com.sortingrocks.sorter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode

class Result : AppCompatActivity() {
    lateinit var barcodeResult : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        barcodeResult = findViewById(R.id.barcodeText)
        val barcode = intent.getStringExtra("barcode")
        barcodeResult.text = barcode
    }

    fun goBack(v : View){
        var intent = Intent(this, MainActivity::class.java)
        startActivityForResult(intent, 0)
    }
}
