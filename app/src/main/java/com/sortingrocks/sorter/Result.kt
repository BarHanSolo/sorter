package com.sortingrocks.sorter

import android.app.ActionBar
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.google.firebase.firestore.FirebaseFirestore

class Result : AppCompatActivity() {
    lateinit var barcodeResult : TextView
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        barcodeResult = findViewById(R.id.barcodeText)
        val barcode = intent.getStringExtra("barcode")
        barcodeResult.text = barcode
        var productName = findViewById<TextView>(R.id.nameText)
        var resultLayout = findViewById<LinearLayout>(R.id.resultLayout)

        //getting data from database
        var db = FirebaseFirestore.getInstance();
        val docRef = db.collection("Products")
        val query = docRef.whereEqualTo("barcode", barcode).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var data = document.data
                    productName.text = document.data.get("name").toString()

                    //for each element adding to layout bar, description, type, (photo)
                    var divider = View(this)
                    var params = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    divider.layoutParams = params
                    divider.updateLayoutParams {
                        height = 1
                    }
                    divider.setBackgroundColor(resources.getColor(R.color.colorDivider))
                    resultLayout.addView(divider)
                    var description = TextView(this)
                    description.text = document.data.get("description").toString()
                    resultLayout.addView(description)
                    var type = TextView(this)
                    type.text = document.data.get("type").toString()
                    resultLayout.addView(type)
                    var picture = ImageView(this)
                    resultLayout.addView(picture)
                    var picUrl = document.data.get("address")
                    Glide.with(getApplicationContext()).load(picUrl).into(picture)
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
