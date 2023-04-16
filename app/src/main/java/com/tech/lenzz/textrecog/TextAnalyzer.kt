package com.tech.lenzz.textrecog

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

var textDone="Recognising..."
class TextAnalyzer:ImageAnalysis.Analyzer {
    val recognizer =TextRecognition.getClient(TextRecognizerOptions.Builder()
        .build())
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("TEXT","text analysed")

        imageProxy?.image?.let{

            val inputImage = InputImage.fromMediaImage(
                it,
                imageProxy.imageInfo.rotationDegrees
            )
            recognizer.process(inputImage)
                .addOnSuccessListener {text->
                    text.textBlocks.forEach{block->

                        Log.d("TEXT","""
                            LINES =${block.lines.joinToString("\n"){block.text}}
                           
                            """.trimIndent())
                       // textDone="recognising..."
                        textDone = block.text.toString()

                    }


                }
                .addOnFailureListener{
                    Log.e("TEXT","Detection failed",it)


                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

        }?: imageProxy.close() //close if image not found


      //  imageProxy.close()
    }
}