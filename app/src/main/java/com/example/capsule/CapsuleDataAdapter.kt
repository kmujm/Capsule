package com.example.capsule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CapsuleDataAdapter(private val context: Context, private val CapsuleList: ArrayList<CapsuleData>):RecyclerView.Adapter<CapsuleDataAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.capsule_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return CapsuleList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(CapsuleList[position])
    }

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val capsulePhoto = view.findViewById<ImageButton>(R.id.CapsuleImage)
        private val capsuleTitle = view.findViewById<TextView>(R.id.CapsuleTitle)
        private val capsuleDate = view.findViewById<TextView>(R.id.CapsuleDate)

        fun bind(item: CapsuleData) {
            capsulePhoto.setImageURI(item.photo)
            capsuleTitle.text = item.title
            capsuleDate.text = item.date
        }
    }


}