package com.tech.lenzz

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("search")
    fun getData(): Call<SearchRVModel?>?
}