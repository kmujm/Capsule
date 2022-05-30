package com.example.capsule

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CapsuleDataAdapter(context: Context, private val CapsuleList: ArrayList<CapsuleData>, private val CapsuleKey: MutableList<String>, private val uid: String ):RecyclerView.Adapter<CapsuleDataAdapter.ItemViewHolder>() {

    val context = context

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
        val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = mDatabase.getReference("Users")
        CapsuleList.removeAt(position)

        // 내가 클릭한 위치 = capsuleKey[position] 데이터 삭제
        Log.i("capsuleKey", CapsuleKey[position])

        myRef.child(uid).child(CapsuleKey[position]).removeValue()
        myRef.child(uid).child(CapsuleKey[position]).setValue(null)

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

            // 쓰레기통 버튼 클릭
            capsuleRemove.setOnClickListener {
                val dlg = Dialog(context)

                dlg.setContentView(R.layout.capsule_delete_dialog)     //다이얼로그에 사용할 xml 파일을 불러옴
                dlg.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
                dlg.window?.setBackgroundDrawable(Drawable.createFromPath("@drawable/alert_dialog_background"))
                // dialog title 지정
                var title: TextView = dlg.findViewById(R.id.title)
                title.text = "캡슐 삭제"

                // dialog message 지정
                var message : TextView = dlg.findViewById(R.id.content)
                message.text = "'${item.title}' 캡슐이 삭제됩니다."

                // 취소 버튼 클릭
                var btnCancel : Button = dlg.findViewById(R.id.cancel)
                btnCancel.setOnClickListener {
                    dlg.dismiss()
                }

                // 삭제 버튼 클릭
                var btnDelete : Button = dlg.findViewById(R.id.delete)
                btnDelete.setOnClickListener {
                    dlg.dismiss()
                    // 현재 position의 아이템 삭제
                    removeData(this.layoutPosition)
                }
                dlg.show()
            }
        }
    }
}