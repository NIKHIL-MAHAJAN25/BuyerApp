package com.nikhil.buyerapp.news

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroInter {
    @GET("latest")
    fun getnews(
        @Query("apikey") key:String,
        @Query("q") searchQuery: String,
        @Query("language") lang: String = "en",
        @Query("size") size: Int = 5
    ): Call<NewsResponse>
}