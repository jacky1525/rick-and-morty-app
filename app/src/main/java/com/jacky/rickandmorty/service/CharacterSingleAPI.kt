package com.jacky.rickandmorty.service

import com.jacky.rickandmorty.model.CharacterModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterSingleAPI {

    @GET("character/{extension}")
    fun getData(@Path ("extension") extension : String): Call<CharacterModel>
}

