package com.sortingrocks.sorter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.gms.common.api.CommonStatusCodes

class WriteBarcodeActivity : AppCompatActivity() {

    lateinit var intentScan : Intent
    lateinit var intentResult : Intent
    lateinit var codeText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_barcode)
        intentScan = Intent(this, ScanBarcodeActivity::class.java)
        intentResult = Intent(this, Result::class.java)
        codeText = findViewById<EditText>(R.id.barcodeWriter)
    }

    fun goBack(v : View){
        var intentBack = Intent(this, MainActivity::class.java)
        startActivityForResult(intentBack, 0)
    }

    fun goToScan(view: View) {
        startActivity(intentScan)
    }

    fun searchCode(view: View) {
        goToResult()
    }

    fun goToResult(){
        intentResult.putExtra("barcode", codeText.text.toString()) //getting barcode
        setResult(CommonStatusCodes.SUCCESS, intentResult)
        startActivity(intentResult)
    }
}
