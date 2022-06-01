package com.example.capsule

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CatagoryAdapter(val mCardClickListener : (String) -> Unit) :
    ListAdapter<CatagoryItem, CatagoryAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val korTitle = itemView.findViewById<TextView>(R.id.itemTitleTextView)
        val engTitle = itemView.findViewById<TextView>(R.id.itemTitmeEngTextView)
        val count = itemView.findViewById<TextView>(R.id.itemCountedTextView)
        val icon = itemView.findViewById<ImageView>(R.id.itemCatagoryIconImageView)
        val cardView = itemView.findViewById<CardView>(R.id.dcCardView)

        fun bind(item : CatagoryItem){
            korTitle.text = item.name
            engTitle.text = item.engName
            count.text = item.count.toString()

            if(item.count == 0 ) {
                cardView.setCardBackgroundColor(itemView.context.getResources().getColor(R.color.disabled))
                icon.background = ContextCompat.getDrawable(itemView.context,R.drawable.half_white_round_bg)
                icon.setColorFilter(itemView.context.getResources().getColor(R.color.main_image_wrapper_gradaion),
                    PorterDuff.Mode.SRC_IN)


            }else {
                cardView.setCardBackgroundColor(itemView.context.getResources().getColor(R.color.primary))
                korTitle.setTextColor(itemView.context.getResources().getColor(R.color.black))
                engTitle.setTextColor(itemView.context.getResources().getColor(R.color.black))
                count.setTextColor(itemView.context.getResources().getColor(R.color.black))

                // 인식한 물체가 있는 곳에만 클릭 리스너를 넘겨줌
                cardView.setOnClickListener {
                    mCardClickListener(item.engName)
                }



            }

            when(item.name){
                DetectedCatagoryActivity.KOR_FASHION -> {
                    icon.setImageDrawable(ContextCompat.getDrawable(itemView.context,R.drawable.ic_shopping_bag_02))
                }
                DetectedCatagoryActivity.KOR_FOOD -> {
                    icon.setImageDrawable(ContextCompat.getDrawable(itemView.context,R.drawable.ic_coffee))
                }
                DetectedCatagoryActivity.KOR_LIVING -> {
                    icon.setImageDrawable(ContextCompat.getDrawable(itemView.context,R.drawable.ic_sun))
                }
                DetectedCatagoryActivity.KOR_PLACE -> {
                    icon.setImageDrawable(ContextCompat.getDrawable(itemView.context,R.drawable.ic_map_pin))
                }
                DetectedCatagoryActivity.KOR_PLANT -> {
                    icon.setImageDrawable(ContextCompat.getDrawable(itemView.context,R.drawable.ic_leaf))
                }
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatagoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_catagory_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatagoryAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CatagoryItem>() {
            override fun areItemsTheSame(oldItem: CatagoryItem, newItem: CatagoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CatagoryItem, newItem: CatagoryItem): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}