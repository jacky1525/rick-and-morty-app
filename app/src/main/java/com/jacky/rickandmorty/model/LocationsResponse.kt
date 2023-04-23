package com.jacky.rickandmorty.model

import com.google.gson.annotations.SerializedName

data class LocationsResponse(
    @SerializedName("results")
    val results : List<LocationModel>
)