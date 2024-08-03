package com.dpfrakes.headsupdiy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpfrakes.headsupdiy.AppData.selectedCategory


class GameMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_category_menu)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // TODO make another API to list all categories available on GitHub and build menu dynamically
        val itemList = createItemList()
        val adapter = ImageAdapter(itemList) { item ->
            // Update the global variable with the drawable's image filename
            selectedCategory = item.filename

            // Navigate to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun createItemList(): List<ImageItem> {
        val itemList = listOf(
            ImageItem(R.drawable.animalkingdom, "Animal Kingdom", "animalkingdom"),
            ImageItem(R.drawable.bible, "The Bible", "bible"),
            ImageItem(R.drawable.boomers, "Boomers", "boomers"),
            ImageItem(R.drawable.brandnames, "Brand Names", "brandnames"),
            ImageItem(R.drawable.childrensbooks, "Childrens Books", "childrensbooks"),
            ImageItem(R.drawable.christmasmovies, "Christmas Movies", "christmasmovies"),
            ImageItem(R.drawable.disney, "Disney", "disney"),
            ImageItem(R.drawable.ducknames, "Duck Names", "ducknames"),
            ImageItem(R.drawable.emojis, "Emojis", "emojis"),
            ImageItem(R.drawable.finance, "Finance", "finance"),
            ImageItem(R.drawable.foodanddrink, "Food and Drink", "foodanddrink"),
            ImageItem(R.drawable.homeimprovement, "Home Improvement", "homeimprovement"),
            ImageItem(R.drawable.techinventions, "Tech & Inventions", "techinventions"),
            ImageItem(R.drawable.italy, "Italy", "italy"),
            ImageItem(R.drawable.military, "Military", "military"),
            ImageItem(R.drawable.millennials, "Millennials", "millennials"),
            ImageItem(R.drawable.movies, "Movies", "movies"),
            ImageItem(R.drawable.musicalinstruments, "Musical Instruments", "musicalinstruments"),
            ImageItem(R.drawable.allthingsmusic, "All Things Music", "allthingsmusic"),
            ImageItem(R.drawable.occupations, "Occupations", "occupations"),
            ImageItem(R.drawable.outdoors, "Outdoors", "outdoors"),
            ImageItem(R.drawable.philosophy, "Philosophy", "philosophy"),
            ImageItem(R.drawable.spaceexploration, "Space Exploration", "spaceexploration"),
            ImageItem(R.drawable.startrek, "Star Trek", "startrek"),
            ImageItem(R.drawable.starwars, "Star Wars", "starwars"),
            ImageItem(R.drawable.technology, "Technology", "technology"),
            ImageItem(R.drawable.the80s, "The '80s", "the80s"),
            ImageItem(R.drawable.the90s, "The '90s", "the90s"),
            ImageItem(R.drawable.travelus, "Travel (US)", "travelus"),
            ImageItem(R.drawable.travelworld, "Travel (World)", "travelworld"),
            ImageItem(R.drawable.weddings, "Weddings", "weddings"),

            ImageItem(R.drawable.doitlive, "Do It Live", "doitlive")
        )

        return itemList
    }

}
