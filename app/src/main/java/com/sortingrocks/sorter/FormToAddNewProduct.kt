package com.sortingrocks.sorter

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class FormToAddNewProduct : AppCompatActivity() {
    lateinit var barcodeResult : TextView
    lateinit var barcode : String
    lateinit var db : CollectionReference
    lateinit var uploadedPhoto : ByteArray
    lateinit var storage : FirebaseStorage
    lateinit var storageReference : StorageReference
    lateinit var typeWaste : String
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_to_add_new_product)

        db = FirebaseFirestore.getInstance().collection("Products")
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        var uniqueID = UUID.randomUUID().toString()
        storageReference = storage.getReference(uniqueID)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        barcodeResult = findViewById(R.id.barcodeText)
        barcode = intent.getStringExtra("barcode")
        barcodeResult.text = barcode

        //establishing type waste spinner
        val spinner: Spinner = findViewById(R.id.spinnerType)
        ArrayAdapter.createFromResource(
            this,
            R.array.typeWaste,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typeWaste = parent?.getItemAtPosition(position).toString()
            }

        }

    }

    fun goBack(v: View) {
        var intentBack = Intent(this, MainActivity::class.java)
        startActivityForResult(intentBack, 0)
    }

    fun uploadToDatabase(view: View) {
        //data preparing
        val nameText = findViewById<EditText>(R.id.editTextName)
        val descriptionText = findViewById<EditText>(R.id.editTextDescription)
        val name = nameText.text.toString()
        val description = descriptionText.text.toString()
        var url : String
        //photo uploading
        if (!::uploadedPhoto.isInitialized){
            Toast.makeText(getApplicationContext(),"Take a photo first",Toast.LENGTH_SHORT).show()
            return
        }
        storageReference.putBytes(uploadedPhoto).addOnFailureListener {
            // failure
            Log.w("TAG", "Error adding photo")
        } .addOnSuccessListener () {taskSnapshot ->
            // success
            storageReference.downloadUrl.addOnCompleteListener () {taskSnapshot ->
                url = taskSnapshot.result.toString()
                //preparing database input
                val newProduct = hashMapOf(
                    "barcode" to barcode,
                    "name" to name,
                    "type" to typeWaste,
                    "description" to description,
                    "address" to url
                )
                //data uploading; if success, closing
                db.add(newProduct).addOnSuccessListener { documentReference ->
                        Log.d("TAG", "DocumentSnapshot written with ID: ${documentReference.id}")
                        Toast.makeText(getApplicationContext(),"The product has been added",Toast.LENGTH_SHORT).show()
                        goBack(view)
                    }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error adding document", e)
                    }
            }
        }

    }

    fun dispatchTakePictureIntent(v: View) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            uploadedPhoto = baos.toByteArray()
        }
    }
}
