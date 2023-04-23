package com.jacky.rickandmorty.service

import com.jacky.rickandmorty.model.LocationsResponse
import retrofit2.Call
import retrofit2.http.GET

interface LocationAPI {

    @GET("location")
    fun getData() : Call<LocationsResponse>



}