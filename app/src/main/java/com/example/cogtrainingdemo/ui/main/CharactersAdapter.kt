package com.example.cogtrainingdemo.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cogtrainingdemo.data.model.CharactersItem
import com.example.cogtrainingdemo.databinding.ItemListBinding


class CharactersAdapter : RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {
    private var characters = listOf<CharactersItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateCharacters(characters: List<CharactersItem>) {
        this.characters = characters
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]

        holder.binding.apply {
            characterName.text = character.name

            Glide.with(holder.itemView.context)
                .load(character.img)
                .into(holder.binding.characterImage)
        }
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
}