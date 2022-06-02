package com.example.capsule

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryImageAdapter(
    private val context: Context,
    private val mData: MutableList<ImageData>,
) :
    RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {
    private val uriArrayList = mData

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int, holder: ImageView)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.image_adapter)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, imageView)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryImageAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_image_adapter, parent, false)
        return ViewHolder(v, mListener)
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
    }

    override fun getItemCount(): Int {
        return uriArrayList!!.size
    }
}

