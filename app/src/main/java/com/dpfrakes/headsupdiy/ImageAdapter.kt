package com.dpfrakes.headsupdiy

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(
    private val itemList: List<ImageItem>,
    private val onItemClick: (ImageItem) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private var filteredList = itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_with_text, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = filteredList[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.textView.text = item.text
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            itemList
        } else {
            itemList.filter {
                it.text.contains(query, ignoreCase = true)
            }
        }
        Log.d("ImageAdapter", "Filtered List: $filteredList")
        notifyDataSetChanged()
        Log.d("ImageAdapter", "Called notifyDataSetChanged!")
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(itemList[position])
                    onItemClick(itemList[position])
                }
            }
        }

        fun bind(imageItem: ImageItem) {
            itemView.findViewById<TextView>(R.id.textView).text = imageItem.text
        }
    }
}
