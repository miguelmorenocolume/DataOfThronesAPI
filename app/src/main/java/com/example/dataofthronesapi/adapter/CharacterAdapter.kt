package com.example.dataofthronesapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dataofthronesapi.databinding.CharacterItemBinding
import com.example.dataofthronesapi.data.Character

class CharacterAdapter(
    private var _characterList: MutableList<Character>,
    private val onClickCharacter: (Int) -> Unit,
    private val onClickFavorite: (Character) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    val characterList get() = _characterList

    class CharacterViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            character: Character,
            onClickCharacter: (Int) -> Unit,
            onClickFavorite: (Character) -> Unit
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
            binding.btnFav.setOnClickListener {
                onClickFavorite(character)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CharacterItemBinding.inflate(layoutInflater, parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characterList[position]
        holder.bind(character, onClickCharacter, onClickFavorite)
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    fun setCharacterList(characters: List<Character>) {
        _characterList = characters.toMutableList()
    }
}
