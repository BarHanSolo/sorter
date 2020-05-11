package com.sortingrocks.sorter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.google.android.gms.common.api.CommonStatusCodes

class WriteBarcodeActivity : AppCompatActivity() {

    lateinit var intentScan : Intent
    lateinit var intentResult : Intent
    lateinit var codeText : EditText
    lateinit var goal : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_barcode)
        intentScan = Intent(this, ScanBarcodeActivity::class.java)
        goal = intent.getStringExtra("goal")  //are we here to "scan" product or to "add" a new one
        intentScan.putExtra("goal", goal)
        intentResult = Intent(this, Result::class.java)
        codeText = findViewById<EditText>(R.id.barcodeWriter)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun goBack(v : View){
        var intentBack = Intent(this, MainActivity::class.java)
        startActivityForResult(intentBack, 0)
    }

    fun goToScan(view: View?) {
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
    //menu handling
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.scan_options_menu, menu)
        return true
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.setVisible(false)
        return super.onPrepareOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.scan -> {
                goToScan(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
