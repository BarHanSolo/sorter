package com.sortingrocks.sorter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector


class ScanBarcodeActivity : AppCompatActivity() {
        lateinit var cameraPreview : SurfaceView
        lateinit var intentResult : Intent
        override fun onCreate(savedInstanceState: Bundle?)
        {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)
        intentResult = Intent(this, Result::class.java)
            cameraPreview = findViewById(R.id.camera_preview)
            createCameraSource();
        }

    fun goBack(v : View){
        var intentBack = Intent(this, MainActivity::class.java)
        startActivityForResult(intentBack, 0)
    }

    fun goToResult(){
        startActivityForResult(intentResult, 0)
    }

    private fun createCameraSource() {
        var barcodeDetector = BarcodeDetector.Builder(this).build()
        var cameraSource = CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).setRequestedPreviewSize(1600, 1024).build()
        cameraPreview.holder!!.addCallback(object: SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                cameraSource.start(holder)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object: Detector.Processor<Barcode>{
            override fun release() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun receiveDetections(p0: Detector.Detections<Barcode>?) {
                var barcodes : SparseArray<Barcode> = p0!!.detectedItems
                if (barcodes.size()>0){
                    intentResult.putExtra("barcode", barcodes.valueAt(0)) //getting only last barcode
                    setResult(CommonStatusCodes.SUCCESS, intentResult)
                    goToResult()
                    finish()
                }
            }

        })
    }
}
