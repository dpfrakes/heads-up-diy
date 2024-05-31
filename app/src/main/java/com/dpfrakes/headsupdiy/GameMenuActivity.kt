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
        val itemList: MutableList<ImageItem> = ArrayList()

        itemList.add(ImageItem(R.drawable.bible, "The Bible", "bible"))
        itemList.add(ImageItem(R.drawable.boomers, "Boomers", "boomers"))
        itemList.add(ImageItem(R.drawable.brandnames, "Brand Names", "brandnames"))
        itemList.add(ImageItem(R.drawable.childrensbooks, "Childrens Books", "childrensbooks"))
        itemList.add(ImageItem(R.drawable.christmasmovies, "Christmas Movies", "christmasmovies"))
        itemList.add(ImageItem(R.drawable.disney, "Disney", "disney"))
        itemList.add(ImageItem(R.drawable.ducknames, "Duck Names", "ducknames"))
        itemList.add(ImageItem(R.drawable.emojis, "Emojis", "emojis"))
        itemList.add(ImageItem(R.drawable.finance, "Finance", "finance"))
        itemList.add(ImageItem(R.drawable.foodanddrink, "Food and Drink", "foodanddrink"))
        itemList.add(ImageItem(R.drawable.homeimprovement, "Home Improvement", "homeimprovement"))
        itemList.add(ImageItem(R.drawable.techinventions, "Tech & Inventions", "techinventions"))
        itemList.add(ImageItem(R.drawable.italy, "Italy", "italy"))
        itemList.add(ImageItem(R.drawable.military, "Military", "military"))
        itemList.add(ImageItem(R.drawable.millennials, "Millennials", "millennials"))
        itemList.add(ImageItem(R.drawable.movies, "Movies", "movies"))
        itemList.add(ImageItem(R.drawable.musicalinstruments, "Musical Instruments", "musicalinstruments"))
        itemList.add(ImageItem(R.drawable.occupations, "Occupations", "occupations"))
        itemList.add(ImageItem(R.drawable.outdoors, "Outdoors", "outdoors"))
        itemList.add(ImageItem(R.drawable.spaceexploration, "Space Exploration", "spaceexploration"))
        itemList.add(ImageItem(R.drawable.startrek, "Star Trek", "startrek"))
        itemList.add(ImageItem(R.drawable.starwars, "Star Wars", "starwars"))
        itemList.add(ImageItem(R.drawable.technology, "Technology", "technology"))
        itemList.add(ImageItem(R.drawable.the80s, "The '80s", "the80s"))
        itemList.add(ImageItem(R.drawable.the90s, "The '90s", "the90s"))
        itemList.add(ImageItem(R.drawable.travelus, "Travel (US)", "travelus"))
        itemList.add(ImageItem(R.drawable.travelworld, "Travel (World)", "travelworld"))
        itemList.add(ImageItem(R.drawable.weddings, "Weddings", "weddings"))

        itemList.add(ImageItem(R.drawable.doitlive, "Do It Live", "doitlive"))

        return itemList
    }

}
