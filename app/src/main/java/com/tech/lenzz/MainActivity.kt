package com.tech.lenzz

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.tech.lenzz.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    lateinit var searchRVAdapter: SearchRVAdapter
    lateinit var searchRVModelList :ArrayList<SearchRVModel>
    val REQUEST_CODE:Int =1
    lateinit var imageBitmap:Bitmap
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        searchRVModelList =ArrayList()
        searchRVAdapter = SearchRVAdapter(this@MainActivity,searchRVModelList)
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
        mainBinding.recyclerView.adapter = searchRVAdapter

        mainBinding.buttonSearch.setOnClickListener{

            searchRVModelList.clear()
            searchRVAdapter.notifyDataSetChanged()
            takePictureIntent()

            getResult()

        }
        mainBinding.buttonGetResult.setOnClickListener {

            searchRVModelList.clear()
            searchRVAdapter.notifyDataSetChanged()
            mainBinding.progressBar.visibility = View.VISIBLE

            getResult()




        }








    }

    private fun getResult() {
       var image :FirebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap)
       var labeler :FirebaseVisionImageLabeler = FirebaseVision.getInstance().getOnDeviceImageLabeler()
      // var labeler :FirebaseVisionImageLabeler = FirebaseVision.getInstance().getCloudImageLabeler()


        labeler.processImage(image).addOnSuccessListener {

            var searchQuery:String = it[0].text
            getSearchResults(searchQuery)

        }.addOnFailureListener{
            Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_SHORT).show()

        }

    }

    private fun getSearchResults(searchQuery: String) {

        val retrofit =Retrofit.Builder()
            .baseUrl("https://serpapi.com/search.json?q="+searchQuery+"&location=Delhi,India&hl=en&gl=us&google_domain=google.com&api_key=0cb579ae52b8cfa3a23ce1a007256e0ee07262481ae22d1f0efc4eeb9feae536")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
        val call :Call<SearchRVModel?>? =retrofitAPI.getData()

        //making call
        call!!.enqueue(object:Callback<SearchRVModel?>{
            override fun onResponse(
                call: Call<SearchRVModel?>,
                response: Response<SearchRVModel?>
            ) {
                if(response.isSuccessful)

                    mainBinding.progressBar.visibility =View.GONE
                    try{
                        val responseBody = response.body()

                            val  titleText : String? =responseBody?.title
                            val descriptionText :String? =responseBody?.snippet
                            val linkText :String? =responseBody?.link
                            val displayedlinkText :String? =responseBody?.displayed_link

                            searchRVModelList.add(SearchRVModel(descriptionText,displayedlinkText,linkText,titleText))


                            searchRVAdapter.notifyDataSetChanged()

                    }catch (e:java.lang.Exception)
                    {
                            e.stackTrace
                    }



            }

            override fun onFailure(call: Call<SearchRVModel?>, t: Throwable) {
                Toast.makeText(this@MainActivity,t.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        })



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE && resultCode== RESULT_OK)
        {
            //bundles updating

            imageBitmap = data?.extras?.get("data") as Bitmap
            mainBinding.image.setImageBitmap(imageBitmap)
        }
    }

    private fun takePictureIntent() {
        intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(intent.resolveActivity(packageManager)!=null)
        {
            startActivityForResult(intent,REQUEST_CODE)
        }
    }
}