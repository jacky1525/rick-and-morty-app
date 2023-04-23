package com.jacky.rickandmorty.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jacky.rickandmorty.view.DetailActivity
import com.jacky.rickandmorty.R
import com.jacky.rickandmorty.model.CharacterModel
import com.squareup.picasso.Picasso

@Suppress("SENSELESS_COMPARISON", "NAME_SHADOWING")
class CharacterRecyclerView(
    private var characterList: List<CharacterModel>,
    private val noCharText: TextView,
) :
    RecyclerView.Adapter<CharacterRecyclerView.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val characterName: TextView = view.findViewById(R.id.charName)
        private val characterImg: ImageView = view.findViewById(R.id.charImg)
        private val genderImg: ImageView = view.findViewById(R.id.genderImg)


        @SuppressLint("SetTextI18n")
        fun bind(characterModel: CharacterModel) {
            characterName.text = characterModel.name

            Picasso.get().load(characterModel.image).into(characterImg)
            when (characterModel.gender) {
                "Male" -> {
                    genderImg.setImageResource(R.drawable.male)
                }
                "Female" -> {
                    genderImg.setImageResource(R.drawable.female)
                }
                "Genderless" -> {
                    genderImg.setImageResource(R.drawable.genderless)
                }
                "unknown" -> {
                    genderImg.setImageResource(R.drawable.unknown)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.char_item, parent, false)
        val holder = MyViewHolder(view)
        holder.itemView.setOnClickListener { view ->

            val context = view.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("characterList", ArrayList(characterList))
            intent.putExtra("position", holder.adapterPosition)
            context.startActivity(intent)
        }

        if (characterList[0].id == null) {

            noCharText.visibility = View.VISIBLE
        } else {

            noCharText.visibility = View.GONE
        }

        return holder
    }

    override fun getItemCount(): Int {
        return if (characterList[0].name == null) {
            0
        } else {
            characterList.size
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val characterModel = characterList[position]

        holder.bind(characterModel)

    }


}