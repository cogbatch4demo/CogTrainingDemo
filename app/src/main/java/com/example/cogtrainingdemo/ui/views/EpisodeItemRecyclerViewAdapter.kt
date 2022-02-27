package com.example.cogtrainingdemo.ui.views

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cogtrainingdemo.R
import com.example.cogtrainingdemo.data.model.EpisodesItem
import com.example.cogtrainingdemo.databinding.FragmentEposideListBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class EpisodeItemRecyclerViewAdapter(
    private var values: List<EpisodesItem>
) : RecyclerView.Adapter<EpisodeItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentEposideListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val stringEpisode = holder.characterList.context.resources.getString(R.string.episode_name)

        holder.characterList.text = item.characters.toString()
        holder.episode.text = String.format(stringEpisode, item.episode_id.toString())
        holder.titleName.text = item.title
        holder.seriesName.text = item.series
    }

    override fun getItemCount(): Int = values.size
    fun updateEpisodeList(data: List<EpisodesItem>) {
        values = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: FragmentEposideListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val characterList: TextView = binding.characterList
        val episode: TextView = binding.episode
        val titleName: TextView = binding.titleName
        val seriesName: TextView = binding.seriesName
    }
}