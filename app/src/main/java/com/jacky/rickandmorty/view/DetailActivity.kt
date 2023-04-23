package com.jacky.rickandmorty.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jacky.rickandmorty.databinding.ActivityDetailBinding
import com.jacky.rickandmorty.model.CharacterModel
import com.squareup.picasso.Picasso
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("UNCHECKED_CAST", "DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val characters = getSerializable(
            this,
            "characterList",
            ArrayList::class.java
        ) as ArrayList<CharacterModel>?

        val position = intent.getIntExtra("position", -1)
        if (position != -1) {
            characters?.get(position)?.let { character ->
                val charImg = character.image
                binding.characterName.text = character.name
                Picasso.get().load(charImg).into(binding.charDetailImg)
                binding.textStatus.text = character.status
                binding.textSpecy.text = character.species
                binding.textGender.text = character.gender
                binding.textOrigin.text = character.origin.name
                binding.textLocation.text = character.location.name

                val getEpisodes = character.episode
                val episodes = getEpisodes.joinToString(", ") {
                    it.substringAfterLast("/")
                }
                binding.textEpisodes.text = episodes
                val createdValue = character.created
                val formattedDate = formatDate(createdValue)
                binding.textCreated.text = formattedDate
            }
        } else {
            println("Error")
        }

        goBack()
    }

    fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.intent.getSerializableExtra(name, clazz)!!
        } else {
            activity.intent.getSerializableExtra(name) as T
        }
    }

    private fun goBack() {

        binding.backButton.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun formatDate(createdData : String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMM yyyy, HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(createdData)
        return outputFormat.format(date!!)
    }


}