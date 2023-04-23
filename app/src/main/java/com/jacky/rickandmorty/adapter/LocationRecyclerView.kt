package com.jacky.rickandmorty.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jacky.rickandmorty.R
import com.jacky.rickandmorty.model.CharacterModel
import com.jacky.rickandmorty.model.LocationModel
import com.jacky.rickandmorty.service.CharacterMultiAPI
import com.jacky.rickandmorty.service.CharacterSingleAPI
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LocationRecyclerView(
    private val locationList: ArrayList<LocationModel>,
    private var adapterCharacter: CharacterRecyclerView?,
    private val recyclerViewCharacter: RecyclerView,
    private val noCharText: TextView
) :
    RecyclerView.Adapter<LocationRecyclerView.MyViewHolder>() {
    private var selectedLocation = 0
    private val url = "https://rickandmortyapi.com/api/"
    private var characterModels: List<CharacterModel>? = null


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locationText: TextView = view.findViewById(R.id.locationText)


        fun bind(locationModel: LocationModel) {
            locationText.text = locationModel.name
        }

        fun getResidents(locationModel: LocationModel): String {
            val residentUrlList = locationModel.residents
            val idList = residentUrlList.mapNotNull { url ->
                "\\d+$".toRegex().find(url)?.value?.toIntOrNull()
            }
            return idList.joinToString(separator = ",")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.location_textview, parent, false)
        return MyViewHolder(view)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: MyViewHolder, position: Int
    ) {

        holder.bind(locationList[position])

        if (selectedLocation == holder.adapterPosition) {
            holder.itemView.setBackgroundResource(R.drawable.locationbackground1)
            val extension = holder.getResidents(locationList[position])
            val extensionSize = extension.split(",").map {
                it.trim()
            }
            val retrofit = Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build()
            if (extensionSize.size > 1) {
                val characterService = retrofit.create(CharacterMultiAPI::class.java)
                val call = characterService.getData(extension)
                call.enqueue(object : Callback<List<CharacterModel>> {


                    override fun onResponse(
                        call: Call<List<CharacterModel>>,
                        response: Response<List<CharacterModel>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                characterModels = ArrayList(it)

                                characterModels?.let { character ->
                                    adapterCharacter = CharacterRecyclerView(character,noCharText)
                                    recyclerViewCharacter.adapter = adapterCharacter
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<CharacterModel>>, t: Throwable) {
                        println("Error occured: ${t.message}")
                        t.printStackTrace()
                    }

                })
            } else {
                val characterService = retrofit.create(CharacterSingleAPI::class.java)
                val call = characterService.getData(extension)
                call.enqueue(object : Callback<CharacterModel> {


                    override fun onResponse(
                        call: Call<CharacterModel>,
                        response: Response<CharacterModel>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                characterModels = listOf(it)

                                characterModels?.let { character ->
                                    adapterCharacter = CharacterRecyclerView(character,noCharText)
                                    recyclerViewCharacter.adapter = adapterCharacter
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CharacterModel>, t: Throwable) {
                        println("Character info:")
                        println("Error occured: ${t.message}")
                        t.printStackTrace()
                    }

                })
            }

        } else {
            holder.itemView.setBackgroundResource(R.drawable.locationbackground2)
        }
        holder.itemView.setOnClickListener {
            selectedLocation = holder.adapterPosition
            /*
            if (locationList[position].residents.size == 0) {
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    noCharText.visibility = View.VISIBLE
                }, 3000)
            } else {
                noCharText.visibility = View.GONE
            }

             */



            notifyDataSetChanged()
        }


    }

    override fun getItemCount(): Int {
        return locationList.count()
    }


}

