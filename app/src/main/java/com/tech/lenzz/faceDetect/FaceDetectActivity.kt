package com.tech.lenzz.faceDetect

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tech.lenzz.BaseLensActivity
import com.tech.lenzz.barcode.BarcodeActivity
import com.tech.lenzz.databinding.ActivityBarcodeBinding
import com.tech.lenzz.databinding.ActivityFaceDetectBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FaceDetectActivity : AppCompatActivity() {
    private lateinit var cameraExecutor: ExecutorService
    companion object{
        @JvmStatic
        val CAMERA_PERM_CODE = 422
    }

    lateinit var faceDetectBinding :ActivityFaceDetectBinding
    val imageAnalyzer =FaceDetectAnalyzer()
    private lateinit var imageAnalysis: ImageAnalysis

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            BarcodeActivity.CAMERA_PERM_CODE
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
                        it.setSurfaceProvider(faceDetectBinding.viewFinder.surfaceProvider) //changed createSurfaceProvider()
                    }
                imageAnalysis = ImageAnalysis.Builder()
                    .setImageQueueDepth(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
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


     fun startScanner()
    {
    startFaceDetect()

    }
    private fun startFaceDetect(){
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer
        )
        if(probability<0.15)
        {
            Toast.makeText(this,"Are you not smiling ??..",Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this,"Yehhh.....happy face??..",Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faceDetectBinding = ActivityFaceDetectBinding.inflate(layoutInflater)
        setContentView(faceDetectBinding.root)

        askCameraPermission()

        faceDetectBinding.btnTakePhoto.setOnClickListener { startScanner() }

        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == BarcodeActivity.CAMERA_PERM_CODE) {
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