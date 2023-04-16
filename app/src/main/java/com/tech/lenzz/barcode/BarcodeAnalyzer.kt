package com.tech.lenzz.barcode

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer :ImageAnalysis.Analyzer{



    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .build()

        val scanner = BarcodeScanning.getClient(options)

        Log.d("BARCODE","barcode analysed")
//        val activity = BarcodeActivity()
//        Toast.makeText(activity,"barcode analysed",Toast.LENGTH_SHORT).show()

        imageProxy?.image?.let{

            val inputImage =InputImage.fromMediaImage(
                it,
                imageProxy.imageInfo.rotationDegrees
            )
            val result = scanner.process(inputImage)
                .addOnSuccessListener {barcodes->
                    for (barcode in barcodes) {
                        val bounds = barcode.boundingBox
                        val corners = barcode.cornerPoints

                        val rawValue = barcode.rawValue

                        val valueType = barcode.valueType

                        Log.d("BARCODE",
                            " Format =${barcode.format} Value =${barcode.rawValue}".trimIndent())
                        // See API reference for complete list of supported types
                        when (valueType) {
                            Barcode.TYPE_WIFI -> {
                                val ssid = barcode.wifi!!.ssid
                                val password = barcode.wifi!!.password
                                val type = barcode.wifi!!.encryptionType
                            }
                            Barcode.TYPE_URL -> {
                                val title = barcode.url!!.title
                                val url = barcode.url!!.url
                            }
                        }

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