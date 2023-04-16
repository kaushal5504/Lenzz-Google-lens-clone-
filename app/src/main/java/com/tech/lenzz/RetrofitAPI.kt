package com.tech.lenzz

import com.tech.lenzz.models.SearchResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {
    @GET("search")
    fun getData(
        @Query ("q") q:String,
        @Query ("location") location:String,
        @Query ("hl") hl:String,
        @Query ("gl") gl:String,
        @Query ("google_domain") google_domain:String,
        @Query ("api_key") ApiKey :String


    ): Call<SearchResults?>?
}