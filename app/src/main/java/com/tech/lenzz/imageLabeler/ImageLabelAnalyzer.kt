package com.tech.lenzz.imageLabeler

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

var labelDone ="unknown"
var labelAcc:Float=1.0f
class ImageLabelAnalyzer: ImageAnalysis.Analyzer{

//    lateinit var searchRVAdapter: SearchRVAdapter
//    lateinit var searchRVModelList :ArrayList<SearchRVModel>
    val labeler =ImageLabeling.getClient(ImageLabelerOptions.Builder()
        .setConfidenceThreshold(0.7F)
        .build()
    )
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        Log.d("LABEL","image analysed")

        imageProxy?.image?.let{

            val inputImage = InputImage.fromMediaImage(
                it,
                imageProxy.imageInfo.rotationDegrees
            )
           labeler.process(inputImage)
                .addOnSuccessListener {it->
                    it.forEach{label->

                       // showingData()


                        Log.d("LABEL","""
                            Format =${label.text}
                            Value =${label.confidence}
                            """.trimIndent())
                        labelDone=label.text
                        labelAcc=label.confidence

                    }




                }
                .addOnFailureListener{
                    Log.e("LABEL","Detection failed",it)


                }
                .addOnCompleteListener {
                   // imageProxy.close()
                }

        }?: imageProxy.close() //close if image not found


       // imageProxy.close()


    }

//    private fun showingData() {
//        searchRVModelList.clear()
////            searchRVAdapter.notifyDataSetChanged()
////            mainBinding.progressBar.visibility = View.VISIBLE
////
////          //  getResult()
//    }
}