package com.sortingrocks.sorter

import android.app.ActionBar
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore


class Result : AppCompatActivity() {
    lateinit var barcodeResult : TextView
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        barcodeResult = findViewById(R.id.barcodeText)
        var textExtraColor = getResources().getColor(R.color.secondaryLightColor);
        val barcode =intent.getStringExtra("barcode")
        val barcodeTextHtml = "<font color="+textExtraColor+">Barcode:</font> " + barcode
        barcodeResult.text = Html.fromHtml(barcodeTextHtml)
        var productName = findViewById<TextView>(R.id.nameText)
        var resultLayout = findViewById<LinearLayout>(R.id.resultLayout)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //getting data from database
        var db = FirebaseFirestore.getInstance();
        val docRef = db.collection("Products")
        val query = docRef.whereEqualTo("barcode", barcode).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var data = document.data
                    var nameTextHtml = "<font color="+textExtraColor+">Name:</font> " + document.data.get("name").toString()
                    productName.text = Html.fromHtml(nameTextHtml)

                    //for each element adding to layout bar, description, type, (photo)
                    var divider = View(this)
                    var params = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    divider.layoutParams = params
                    divider.updateLayoutParams {
                        height = 3
                    }
                    divider.setBackgroundColor(resources.getColor(R.color.secondaryColor))
                    //divider.setPadding(0, 0, 0, getResources().getDimension(R.dimen.paddingSmall).toInt())
                    Log.i("TAG", getResources().getDimension(R.dimen.paddingSmall).toInt().toString())
                    resultLayout.addView(divider)
                    var description = TextView(this)
                    var descriptionTextHtml = "<font color="+textExtraColor+">Description:</font> " + document.data.get("description").toString()
                    description.text = Html.fromHtml(descriptionTextHtml)
                    description.setPadding(0, getResources().getDimension(R.dimen.paddingSmall).toInt(), 0, 0)
                    resultLayout.addView(description)
                    var type = TextView(this)
                    var typeTextHtml = "<font color="+textExtraColor+">Bin:</font> " + document.data.get("type").toString()
                    type.text = Html.fromHtml(typeTextHtml)
                    resultLayout.addView(type)
                    var picture = ImageView(this)
                    picture.setPadding(0, getResources().getDimension(R.dimen.paddingSmall).toInt(), 0, getResources().getDimension(R.dimen.paddingSmall).toInt())
                    resultLayout.addView(picture)
                    var picUrl = document.data.get("address")
                    Glide.with(getApplicationContext()).load(picUrl).into(picture)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    fun goBack(v: View?){
        var intent = Intent(this, MainActivity::class.java)
        startActivityForResult(intent, 0)
    }
}
