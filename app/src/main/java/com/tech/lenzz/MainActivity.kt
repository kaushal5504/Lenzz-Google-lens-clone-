package com.tech.lenzz

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tech.lenzz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    lateinit var searchRVAdapter: SearchRVAdapter
    lateinit var searchRVModelList :ArrayList<SearchRVModel>
    val REQUEST_CODE:Int =1
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






        }
        mainBinding.buttonGetResult.setOnClickListener {

        }








    }

    private fun takePictureIntent() {
        intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(intent.resolveActivity(packageManager)!=null)
        {

        }
    }
}