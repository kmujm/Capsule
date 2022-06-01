package com.example.capsule

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryImageAdapter(private val context: Context, mData: ArrayList<String>?) :
    RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {
    private val uriArrayList = mData


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        Glide.with(holder.imageView)
            .load(uriArrayList?.get(position))
            .thumbnail(0.33f)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return uriArrayList!!.size
    }

}