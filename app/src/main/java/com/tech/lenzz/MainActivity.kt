package com.tech.lenzz

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tech.lenzz.barcode.BarcodeActivity
import com.tech.lenzz.databinding.ActivityMainBinding
import com.tech.lenzz.faceDetect.FaceDetectActivity
import com.tech.lenzz.imageLabeler.ImageLabelingActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    companion object{
        val REQUEST_CODE:Int =1
        val EXTRA_DATA ="data"

    }

    lateinit var imageBitmap:Bitmap
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)


        mainBinding.buttonSearch.setOnClickListener{

            val takePhotoIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePhotoIntent, REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                Log.d("Cam","camera not found error")
            }

            //startActivity(Intent(this, CameraActivity::class.java))


        }
        mainBinding.buttonML.setOnClickListener {
            startActivity(Intent(this,OptionsActivity::class.java))
        }











    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            mainBinding.image.setImageBitmap(imageBitmap)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}