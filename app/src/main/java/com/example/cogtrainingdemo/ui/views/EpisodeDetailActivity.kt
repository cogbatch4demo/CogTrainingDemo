package com.example.cogtrainingdemo.ui.views

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cogtrainingdemo.data.model.EpisodesItem
import com.example.cogtrainingdemo.databinding.ActivityEpisodeDetailBinding

class EpisodeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEpisodeDetailBinding
    var episodeDetailItem: EpisodesItem? = null

    companion object {
        const val DATA_CONTENT = "Data content"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        getIntentData()
        binding.fab.setOnClickListener { view ->
            finish()
        }
        showDataOnScreen()
    }

    private fun getIntentData() {
        episodeDetailItem = intent?.getParcelableExtra<EpisodesItem>(DATA_CONTENT)
    }

    private fun showDataOnScreen() {
        episodeDetailItem?.let {
            with(binding.detail) {
                title.text = it.title
                season.text = it.season
                airDate.text = it.air_date
                series.text = it.series
                espisode.text = it.episode
            }
            setCharactersList(it)
        }
    }

    private fun setCharactersList(episodesItem: EpisodesItem) {
        episodesItem.characters?.forEach {
            val textview = TextView(this)
            textview.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            textview.gravity = Gravity.CENTER_HORIZONTAL
            textview.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
            textview.textSize = 18.0F
            textview.text = it
            binding.detail.list.addView(textview)
        }
    }
}