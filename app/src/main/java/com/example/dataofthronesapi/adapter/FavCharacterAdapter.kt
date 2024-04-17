package com.example.dataofthronesapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dataofthronesapi.data.Character
import com.example.dataofthronesapi.databinding.FavCharacterItemBinding

class FavCharacterAdapter(
    private var _favCharacterList: List<Character>,
    private val onClickCharacter: (Int) -> Unit,
    private val onClickDelete: (Character) -> Unit
) : RecyclerView.Adapter<FavCharacterAdapter.CharacterViewHolder>() {

    val characterList get() = _favCharacterList

    class CharacterViewHolder(private val binding: FavCharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            character: Character,
            onClickCharacter: (Int) -> Unit,
            onClickDelete: (Character) -> Unit
        ) {
            binding.tvName.text = character.firstName
            binding.tvHouse.text = character.family
            val context = binding.image.context
            Glide.with(context)
                .load(character.imageUrl)
                .centerCrop()
                .into(binding.image)
            binding.root.setOnClickListener {
                onClickCharacter(character.id)
            }
            binding.btnDelete.setOnClickListener {
                onClickDelete(character)

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavCharacterAdapter.CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FavCharacterItemBinding.inflate(layoutInflater, parent, false)
        return FavCharacterAdapter.CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characterList[position]
        holder.bind(character, onClickCharacter, onClickDelete)
    }


    override fun getItemCount(): Int {
        return characterList.size
    }

    fun setCharacterList(characters: List<Character>) {
        _favCharacterList = characters.toMutableList()
    }
}