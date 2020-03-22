package com.sortingrocks.sorter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.google.firebase.firestore.FirebaseFirestore

class Result : AppCompatActivity() {
    lateinit var barcodeResult : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        barcodeResult = findViewById(R.id.barcodeText)
        val barcode = intent.getStringExtra("barcode")
        barcodeResult.text = barcode
        var productName = findViewById<TextView>(R.id.nameText)

        //getting data from database
        var db = FirebaseFirestore.getInstance();
        val docRef = db.collection("Products")
        val query = docRef.whereEqualTo("barcode", barcode).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var data = document.data
                    productName.text = document.data.get("name").toString()
                    //Log.d("TAG", data.get("name").toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    fun goBack(v : View){
        var intent = Intent(this, MainActivity::class.java)
        startActivityForResult(intent, 0)
    }
}
