package com.nikhil.buyerapp.news

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroNews {
    private const val base_url="https://newsdata.io/api/1/"
    val instance:RetroInter by lazy {
        Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetroInter::class.java)
    }
}