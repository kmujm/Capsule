package com.example.capsule

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ImageAdpater :
    ListAdapter<ImageItem, ImageAdpater.ViewHolder>(diffUtil) {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.pcItemImage)
        val mainTextView = itemView.findViewById<TextView>(R.id.pcMainTextView)

        fun bind(item : ImageItem){
            if(item.isMain) {
                image.backgroundTintList = ColorStateList.valueOf(itemView.context.getResources().getColor(R.color.icon_transparnet_half))
                mainTextView.isVisible = true
            }

            // 넘겨받은 uri로 이미지 매핑, 글라이드를 활용하면 좋지 않을까 ?
            // https://parkho79.tistory.com/168
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdpater.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_posted_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageAdpater.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ImageItem>() {
            override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}