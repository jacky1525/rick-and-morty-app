package com.jacky.rickandmorty.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jacky.rickandmorty.adapter.CharacterRecyclerView
import com.jacky.rickandmorty.adapter.LocationRecyclerView
import com.jacky.rickandmorty.databinding.ActivityHomePageBinding
import com.jacky.rickandmorty.model.CharacterModel
import com.jacky.rickandmorty.model.LocationModel
import com.jacky.rickandmorty.model.LocationsResponse
import com.jacky.rickandmorty.service.LocationAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomePage : AppCompatActivity() {

    private var adapterLocation: LocationRecyclerView? = null
    private lateinit var recyclerViewLocation: RecyclerView
    private val url = "https://rickandmortyapi.com/api/"
    private var locationModels: ArrayList<LocationModel>? = null
    private var recyclerViewCharacter: RecyclerView? = null
    private var adapterCharacter: CharacterRecyclerView? = null
    private var characterModels: List<CharacterModel>? = null


    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val noCharText = binding.noCharInfoText
        // --------------------------------------------------------------------------
        //Location RecyclerView ***

        recyclerViewLocation = binding.locationRecyclerView

        recyclerViewLocation.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapterLocation =
            recyclerViewCharacter?.let {
                LocationRecyclerView(
                    ArrayList(),
                    adapterCharacter,
                    it,
                    noCharText
                )
            }
        recyclerViewLocation.adapter = adapterLocation


        //----------------------------------------------------------------------------
        //Character RecyclerView ***

        recyclerViewCharacter = binding.characterRecyclerView

        recyclerViewCharacter!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapterCharacter = characterModels?.let { CharacterRecyclerView(it,noCharText) }
        recyclerViewCharacter!!.adapter = adapterCharacter

        //-----------------------------------------------------------------------------
        loadLocationData()
    }



    private fun loadLocationData() {
        val noCharText = binding.noCharInfoText
        val retrofit =
            Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val locationService = retrofit.create(LocationAPI::class.java)
        val call = locationService.getData()

        call.enqueue(object : Callback<LocationsResponse> {
            override fun onResponse(
                call: Call<LocationsResponse>,
                response: Response<LocationsResponse>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let {
                        locationModels = ArrayList(it.results)

                        locationModels?.let { locations ->
                            adapterLocation = LocationRecyclerView(
                                locations, adapterCharacter,
                                recyclerViewCharacter!!,
                                noCharText
                            )
                            recyclerViewLocation.adapter = adapterLocation
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LocationsResponse>, t: Throwable) {
                println("Location info:")
                println("Error occured: ${t.message}")
                t.printStackTrace()
            }
        })
    }

}

