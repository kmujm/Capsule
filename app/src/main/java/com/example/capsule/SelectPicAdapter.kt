package com.example.capsule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.CoroutineScope

class SelectPicAdapter(
    private val context: CoroutineScope,
    private val mData: MutableList<ImageData>
) :
    RecyclerView.Adapter<SelectPicAdapter.ViewHolder>() {
    private val uriList = mData

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int, holder: ImageView)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imgGalleryItem)
        val imageFrame = itemView.findViewById<ImageView>(R.id.imgFrame)
        val imageCheck = itemView.findViewById<ImageView>(R.id.imgSelectBackGround)
        val cntCheck = itemView.findViewById<TextView>(R.id.tvSelectImageCount)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectPicAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: SelectPicAdapter.ViewHolder, position: Int) {
        Glide.with(holder.imageView)
            .load(uriList[position].contentUri)
            .transform(CenterCrop(), RoundedCorners(12))
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return uriList!!.size
    }

}