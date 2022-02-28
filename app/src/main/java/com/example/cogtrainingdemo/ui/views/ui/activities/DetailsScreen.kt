package com.example.cogtrainingdemo.ui.views.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.cogtrainingdemo.data.model.CharactersItem
import com.example.cogtrainingdemo.databinding.ActivityDetailsScreenBinding

class DetailsScreen : AppCompatActivity() {

   lateinit var detailsScreenBinding: ActivityDetailsScreenBinding
    private  var characterData: CharactersItem ?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsScreenBinding = ActivityDetailsScreenBinding.inflate(layoutInflater)
        setContentView(detailsScreenBinding.root)
        val actionbar = supportActionBar
        actionbar!!.title = "Artist Details"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        characterData = intent.getSerializableExtra("CHARACTER_DATA") as CharactersItem?
        detailsScreenBinding.title.text = characterData?.name.toString()
        detailsScreenBinding.dob.text = characterData?.birthday.toString()
        detailsScreenBinding.potrayedDesc.text = characterData?.portrayed
        detailsScreenBinding.categoryDesc.text = characterData?.category


        Glide.with(this@DetailsScreen)
            .load(characterData?.img)
            .centerCrop()
            .centerInside()
            .into(detailsScreenBinding.itemImage)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}