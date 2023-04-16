package com.tech.lenzz.imageLabeler

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tech.lenzz.RetrofitAPI
import com.tech.lenzz.barcode.BarcodeActivity
import com.tech.lenzz.databinding.ActivityImageLabelingBinding
import com.tech.lenzz.models.OrganicResult
import com.tech.lenzz.models.OrganicResultAdapter
import com.tech.lenzz.models.SearchResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLabelingActivity:AppCompatActivity() {

    lateinit var organicResultAdapter: OrganicResultAdapter
    lateinit var organicResultList :ArrayList<OrganicResult>
    private lateinit var cameraExecutor: ExecutorService
    companion object {
        @JvmStatic
        val CAMERA_PERM_CODE = 422
    }
    lateinit var imageLabelBinding: ActivityImageLabelingBinding
    val imageAnalyzer =ImageLabelAnalyzer()
    var imageAnalysis = ImageAnalysis.Builder()
        .setImageQueueDepth(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()


    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            BarcodeActivity.CAMERA_PERM_CODE
        )
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLabelBinding = ActivityImageLabelingBinding.inflate(layoutInflater)
        setContentView(imageLabelBinding.root)

        askCameraPermission()

        cameraExecutor = Executors.newSingleThreadExecutor()

        organicResultList =ArrayList()
        organicResultAdapter = OrganicResultAdapter(this,organicResultList)
        imageLabelBinding.recyclerView.layoutManager = LinearLayoutManager(this@ImageLabelingActivity,LinearLayoutManager.HORIZONTAL,false)
        imageLabelBinding.recyclerView.adapter = organicResultAdapter

        imageLabelBinding.btnTakePhoto.setOnClickListener { startScanner() }

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(imageLabelBinding.viewFinder.surfaceProvider) //changed createSurfaceProvider()
                    }
                imageAnalysis = ImageAnalysis.Builder()
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

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
     fun startScanner() {
        startImageLabeling()
    }

    private fun startImageLabeling(){
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer


        )
        imageLabelBinding.tvLabel.text = labelDone.toString()
        imageLabelBinding.tvLabelAcc.text =labelAcc.toString()
        getResult(labelDone)
    }

    private fun getResult(label:String) {
        getSearchResults(label)

    }

    private fun getSearchResults(searchQuery: String) {
                val retrofit = Retrofit.Builder()
            .baseUrl("https://serpapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
        val call : Call<SearchResults?>? =retrofitAPI.getData(searchQuery,"Delhi,India","hl","gl","google.com","0cb579ae52b8cfa3a23ce1a007256e0ee07262481ae22d1f0efc4eeb9feae536")

        //making call
        call!!.enqueue(object: Callback<SearchResults?> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<SearchResults?>,
                response: Response<SearchResults?>
            ) {
                if(response.isSuccessful)

                    imageLabelBinding.ProgressBar.visibility = View.GONE
                try{
                    val searchResult : SearchResults? =response.body()

                    val organicResult : List<OrganicResult?>? = searchResult?.organic_results

                    val  titleText : String? = organicResult?.get(0)?.title
                            val descriptionText :String? =organicResult?.get(0)?.snippet
                            val linkText :String? =organicResult?.get(0)?.link
                            val displayedlinkText :String? =organicResult?.get(0)?.displayed_link

                    organicResultList.add(OrganicResult(displayedlinkText,linkText,1,descriptionText,titleText))


                    organicResultAdapter.notifyDataSetChanged()


                }
                catch (e:java.lang.Exception)
                {
                    e.stackTrace
                }

            }

            override fun onFailure(call: Call<SearchResults?>, t: Throwable) {
                // Toast.makeText(this,t.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        })

    }





}