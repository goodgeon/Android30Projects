package com.example.a15airbnb

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/5f28a044-a7b2-4f5d-ab75-b2b519ace71d")
    fun getHouseList(): Call<HouseDto>
}