package com.tech.lenzz.barcode

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer :ImageAnalysis.Analyzer{
    val scanner = BarcodeScanning.getClient()


    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("Barcode","image analysed")

        imageProxy?.image?.let{

            val inputImage =InputImage.fromMediaImage(
                it,
                imageProxy.imageInfo.rotationDegrees
            )
            scanner.process(inputImage)
                .addOnSuccessListener {
                    it.forEach{barcode->
                        Log.d("BARCODE","""
                            Format =${barcode.format}
                            Value =${barcode.rawValue}
                            """.trimIndent())

                    }


                }
                .addOnFailureListener{
                    Log.e("BARCODE","Detection failed",it)


                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

        }?: imageProxy.close() //close if image not found


        imageProxy.close()

    }
}