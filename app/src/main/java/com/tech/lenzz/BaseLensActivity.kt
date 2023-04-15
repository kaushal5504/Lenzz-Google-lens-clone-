package com.tech.lenzz

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


abstract class BaseLensActivity : AppCompatActivity()
{



    abstract val imageAnalyzer :ImageAnalysis.Analyzer
    protected lateinit var imageAnalysis:ImageAnalysis
    companion object {
        @JvmStatic
        val CAMERA_PERM_CODE = 422
    }


    protected fun askCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CAMERA_PERM_CODE
        )
    }

    abstract fun startScanner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lens)

        askCameraPermission()

         val btnStartScanner : Button =findViewById(R.id.btnTakePhoto)
        btnStartScanner.setOnClickListener {
                startScanner()
        }

    }

    protected fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        val camPreview: View =findViewById(R.id.previewView)
                        it.setSurfaceProvider(camPreview) //changed createSurfaceProvider()
                    }

                //imageCapture = ImageCapture.Builder().build()
                imageAnalysis = ImageAnalysis.Builder()
                    // .setImageQueueDepth()
                    .build()


                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                } catch (ex: Exception) {
                    Log.e("CAM", "Error binding camera", ex)
                }

            },
            ContextCompat.getMainExecutor(this)

        )
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



}