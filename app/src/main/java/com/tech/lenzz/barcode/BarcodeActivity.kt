package com.tech.lenzz.barcode


import androidx.core.content.ContextCompat

import com.tech.lenzz.BaseLensActivity



class BarcodeActivity : BaseLensActivity(){
    companion object {
        @JvmStatic
        val CAMERA_PERM_CODE = 422
    }

    override val imageAnalyzer =BarcodeAnalyzer()
    override fun startScanner() {
        scanBarCode()
    }


    private fun scanBarCode() {

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this)
        ,imageAnalyzer
        )

    }




}