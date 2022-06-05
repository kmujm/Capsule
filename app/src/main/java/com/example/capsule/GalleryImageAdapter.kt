package com.example.capsule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class GalleryImageAdapter(
    private val context: Context,
    private val mData: MutableList<ImageData>,
) :
    RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {
    private val uriArrayList = mData

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int, holder: ImageView, check: ImageView)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.image_adapter)
        val check = itemView.findViewById<ImageView>(R.id.check_icon)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, imageView, check)
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
                .transform(CenterCrop(), RoundedCorners(12))
                .into(holder.imageView)
        } else {
            Glide.with(holder.imageView)
                .load(uriArrayList[position].contentUri)
                .transform(CenterCrop(), RoundedCorners(12))
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return uriArrayList!!.size
    }
}

