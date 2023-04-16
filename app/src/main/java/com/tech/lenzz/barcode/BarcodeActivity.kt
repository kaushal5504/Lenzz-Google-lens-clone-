package com.tech.lenzz.barcode

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.Builder
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.tech.lenzz.databinding.ActivityBarcodeBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class BarcodeActivity : AppCompatActivity(){

companion object {
    @JvmStatic
    val CAMERA_PERM_CODE = 422
}
    private lateinit var cameraExecutor: ExecutorService
    lateinit var barcodeBinding: ActivityBarcodeBinding
    private  val imageAnalyzer = BarcodeAnalyzer()
    private var imageAnalysis = Builder()
        .setImageQueueDepth(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
        .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
        .build()





    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CAMERA_PERM_CODE
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(barcodeBinding.viewFinder.surfaceProvider) //changed createSurfaceProvider()
                    }
                imageAnalysis = Builder()
                    .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                } catch (ex: Exception) {
                    Log.e("CAM", "Error bindind camera", ex)
                }

            },
            ContextCompat.getMainExecutor(this)

        )

    }

    private fun scanBarcode() {


       imageAnalysis.setAnalyzer(
           ContextCompat.getMainExecutor(this),
           imageAnalyzer

       )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcodeBinding = ActivityBarcodeBinding.inflate(layoutInflater)
        setContentView(barcodeBinding.root)

        askCameraPermission()

        barcodeBinding.btnTakePhoto.setOnClickListener { scanBarcode() }

        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Permission Error")
                    .setMessage("Camera Permission not provided")
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .setCancelable(false)
                    .show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }




}