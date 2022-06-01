package com.example.capsule

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ShowCapsuleAdapter(context: Context, private val CapsuleDataList: ArrayList<ShowCapsuleData>):RecyclerView.Adapter<ShowCapsuleAdapter.ItemViewHolder>(){


    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.show_capsule_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int{
        return CapsuleDataList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = CapsuleDataList[position]
        holder.apply {
            bind(item, position)
        }
    }

    inner class ItemViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private val capsuleTitle = v.findViewById<TextView>(R.id.CapsuleTitle)
        private val capsuleDate = v.findViewById<TextView>(R.id.CapsuleDate)
        private val capsulePhoto = v.findViewById<ImageView>(R.id.CapsuleImage)
        private val capsuleRegPhoto = v.findViewById<ImageView>(R.id.CapsuleRegisterImage)

        fun bind(item: ShowCapsuleData, position: Int) {
            Glide.with(itemView).load(item.photo).into(capsulePhoto);
            capsuleTitle.text = item.title
            capsuleDate.text = item.date
            Glide.with(itemView).load(item.regPhoto[position]).into(capsuleRegPhoto);
        }
    }
}