package com.example.capsule

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import java.security.AccessController.getContext

class RecentCapsuleAdapter (val mContext : Context, val RecentCapsuleList : MutableList<RecentCapsuleItem>) : RecyclerView.Adapter<RecentCapsuleAdapter.CustomViewHolder>(){

    class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val capsuleDate = itemView.findViewById<TextView>(R.id.capsule_date)
        val capsuleTitle = itemView.findViewById<TextView>(R.id.capsule_title)
        val morePicture = itemView.findViewById<TextView>(R.id.more_picture)
        val img1 = itemView.findViewById<ImageView>(R.id.img1)
        val img2 = itemView.findViewById<ImageView>(R.id.img2)
        val img3 = itemView.findViewById<ImageView>(R.id.img3)

        fun bind(item : RecentCapsuleItem, position: Int){
            capsuleDate.text = item.capsuleDate
            capsuleTitle.text = item.capsuleTitle
            if(item.pictureList.size > 3){
                morePicture.text = "+${item.pictureList.size-3}"
            }
            Glide.with(itemView).load(item.pictureList[0]).into(img1)
            if(item.pictureList.size > 1){
                Glide.with(itemView).load(item.pictureList[1]).into(img2)
            }
            if(item.pictureList.size > 2){
                Glide.with(itemView).load(item.pictureList[2]).into(img3)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_capsule, parent, false)
        return CustomViewHolder(view).apply{
            itemView.setOnClickListener {
                val curPos = adapterPosition
                val item = RecentCapsuleList.get(curPos)
                // 누르면 화면 전환
                val intent = Intent(mContext, ShowCapsuleActivity::class.java)
                // 2. 캡슐 보기 화면으로 인텐트에 캡슐키 담아 전환
                intent.putExtra("capsuleKey", item.capsuleKey)
                intent.putExtra("capsuleTitle", item.capsuleTitle)
                intent.putExtra("capsuleDate", item.capsuleDate)
                intent.putExtra("capsulePhoto", item.detectImage)
                intent.putExtra("capsuleContent", item.content)

                mContext.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecentCapsuleAdapter.CustomViewHolder, position: Int) {
        val item = RecentCapsuleList[position]
        holder.apply {
            bind(item, position)
        }
    }

    override fun getItemCount(): Int {
        return RecentCapsuleList.size
    }
}