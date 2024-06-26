package com.example.codelabx.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroInstance {

    fun getInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://ec2-15-206-90-96.ap-south-1.compute.amazonaws.com:8010")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}