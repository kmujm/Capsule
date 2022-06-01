package com.example.capsule

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.Image
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryImageAdapter(
    private val context: Context,
    private val mData: MutableList<ImageData>,

) :
    RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {
    private val uriArrayList = mData

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.image_adapter)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryImageAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_image_adapter, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: GalleryImageAdapter.ViewHolder, position: Int) {
        if (uriArrayList[position].contentUri == "1") {
            Glide.with(holder.imageView).load(R.drawable.ic_gallery_btn)
                .into(holder.imageView)
        } else {
            Glide.with(holder.imageView)
                .load(uriArrayList[position].contentUri)
                .thumbnail(0.33f)
                .centerCrop()
                .into(holder.imageView)
        }

        val item = uriArrayList[position]
        holder.imageView.setOnClickListener {
            selectItem(holder, item, position)
        }
    }

    private fun selectItem(holder: GalleryImageAdapter.ViewHolder, item: ImageData, position: Int) {
        if (item.selected) {
            item.selected = false
            holder.imageView.clearColorFilter()
        } else {
            item.selected = true
            holder.imageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
        }
        Log.d("제발 원하는대로 좀 해주라...", uriArrayList.toString())


    }

    override fun getItemCount(): Int {
        return uriArrayList!!.size
    }
}

