package com.example.capsule

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CapsuleDataAdapter(private val CapsuleList: ArrayList<CapsuleData>):RecyclerView.Adapter<CapsuleDataAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.capsule_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int{
        return CapsuleList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = CapsuleList[position]
        holder.apply {
            bind(item)
        }
    }

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        CapsuleList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ItemViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private val capsulePhoto = v.findViewById<ImageButton>(R.id.CapsuleImage)
        private val capsuleTitle = v.findViewById<TextView>(R.id.CapsuleTitle)
        private val capsuleDate = v.findViewById<TextView>(R.id.CapsuleDate)

        private val capsuleRemove = v.findViewById<Button>(R.id.tvRemove)
        fun bind(item: CapsuleData) {
            Glide.with(itemView).load(item.photo).into(capsulePhoto);
            capsuleTitle.text = item.title
            capsuleDate.text = item.date

            // 삭제 버튼 클릭
            capsuleRemove.setOnClickListener {
                removeData(this.layoutPosition)
            }
        }
    }
}