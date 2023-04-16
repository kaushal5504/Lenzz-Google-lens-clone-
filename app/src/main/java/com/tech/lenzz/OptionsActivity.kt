package com.tech.lenzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tech.lenzz.barcode.BarcodeActivity
import com.tech.lenzz.databinding.ActivityOptionsBinding
import com.tech.lenzz.faceDetect.FaceDetectActivity
import com.tech.lenzz.imageLabeler.ImageLabelingActivity
import com.tech.lenzz.textrecog.TextRecognizationActivity

class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var optionBinding :ActivityOptionsBinding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(optionBinding.root)

        optionBinding.btnbarcode.setOnClickListener {
            startActivity(Intent(this, BarcodeActivity::class.java))
        }
        optionBinding.btnFace.setOnClickListener {
            startActivity(Intent(this, FaceDetectActivity::class.java))
        }
        optionBinding.btnLabeler.setOnClickListener {
            startActivity(Intent(this, ImageLabelingActivity::class.java))
        }
        optionBinding.btnTextR.setOnClickListener {
            startActivity(Intent(this, TextRecognizationActivity::class.java))
        }
    }
}