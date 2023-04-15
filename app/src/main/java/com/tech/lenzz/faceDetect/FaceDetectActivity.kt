package com.tech.lenzz.faceDetect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.tech.lenzz.BaseLensActivity

class FaceDetectActivity : BaseLensActivity() {

    override val imageAnalyzer =FaceDetectAnalyzer()
    override fun startScanner()
    {
    startFaceDetect()

    }
    private fun startFaceDetect(){
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}