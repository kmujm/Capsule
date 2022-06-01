package com.example.capsule

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class CapsuleListActivity : AppCompatActivity() {
    private var CapsuleList = ArrayList<CapsuleData>()    // recyclerView 에 띄워줄 데이터 저장

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")
    private var user = FirebaseAuth.getInstance().currentUser    // 현재 로그인한 유저

    lateinit var date:String     // capsule date
    lateinit var title:String    // capsule title
    lateinit var photoUri: Uri   // capsule detectImage
    private var capsuleKey: MutableList<String> = mutableListOf()   // capsule key 들만 모아놓은 배열

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_capsule_list)

        if (user != null) {
            var uid = user!!.uid
            // todo: .child(uid)로 수정
            // capsule key들만 모아놓은 배열 생성
            myRef.child("asdfifeiofjn1233").get().addOnSuccessListener {
                it.children.forEach {
                    capsuleKey.add(it.key.toString())
                }
                // key 배열 중 INFO 삭제
                capsuleKey.removeAt(0)
                initValue(uid)
            }
        }
    }

    private fun initValue(uid:String) {
        var cnt = 0
        for (capsule in capsuleKey) {
            // todo: .child(uid)로 수정
            myRef.child("asdfifeiofjn1233").child(capsule).get().addOnSuccessListener {
                // capsule 별 date, title, detectImage 저장
                date = it.child("date").getValue<String>().toString()
                title = it.child("title").getValue<String>().toString()
                photoUri = it.child("detectImage").getValue<String>()!!.toUri()

                // 캡슐 하나 저장 -> cnt+=1
                cnt+=1
                // CapsuleList에 유저의 모든 캡슐 정보 저장
                CapsuleList.add(CapsuleData(title,date,photoUri))
                // capsule key 개수만큼 리스트에 저장되었으면 initRecycler 호출
                if (cnt == capsuleKey.size) {
                    initRecycler(uid)
                }
            }
        }
    }

    private fun initRecycler(uid:String) {
        // adaper 와 recyclerView 연결
        val recyclerView: RecyclerView by lazy {
            findViewById(R.id.CapsuleList)
        }
        // TODO: uid 전달로 변경
        val capsuleAdapter = CapsuleDataAdapter(this, CapsuleList, capsuleKey, "asdfifeiofjn1233")
        recyclerView.adapter = capsuleAdapter

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(capsuleAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 5)    // 1080 / 5
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(recyclerView)
    }
}