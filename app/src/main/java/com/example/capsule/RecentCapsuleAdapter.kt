package com.example.capsule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecentCapsuleAdapter (val mContext : Context, val RecentCapsuleList : ArrayList<RecentCapsuleItem>) : RecyclerView.Adapter<RecentCapsuleAdapter.CustomViewHolder>(){

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val capsuleDate = itemView.findViewById<TextView>(R.id.capsule_date)
        val capsuleTitle = itemView.findViewById<TextView>(R.id.capsule_title)
        val morePicture = itemView.findViewById<TextView>(R.id.more_picture)
        val img1 = itemView.findViewById<ImageView>(R.id.img1)
        val img2 = itemView.findViewById<ImageView>(R.id.img2)
        val img3 = itemView.findViewById<ImageView>(R.id.img3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_capsule, parent, false)
        return CustomViewHolder(view).apply{
            itemView.setOnClickListener {
                // TODO 누르면 화면 전환
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.capsuleDate.text = RecentCapsuleList.get(position).capsuleDate.toString()
        holder.capsuleTitle.text = RecentCapsuleList.get(position).capsuleTitle
        holder.morePicture.text = "+$RecentCapsuleList.get(position).pictureList.size"
        holder.img1.setImageURI(RecentCapsuleList.get(position).pictureList[0])
        holder.img2.setImageURI(RecentCapsuleList.get(position).pictureList[1])
        holder.img3.setImageURI(RecentCapsuleList.get(position).pictureList[2])
    }

    override fun getItemCount(): Int {
        return RecentCapsuleList.size
    }
}