package com.example.capsule

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class ShowCapsuleActivity : AppCompatActivity() {
    private var CapsuleDataList = ArrayList<ShowCapsuleData>()    // recyclerView 에 띄워줄 데이터 저장

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")
    private var user = FirebaseAuth.getInstance().currentUser    // 현재 로그인한 유저

    lateinit var date:String     // capsule date
    lateinit var title:String    // capsule title
    lateinit var photoUri: Uri   // capsule detectImage
    lateinit var capsuleContent: String   // capsule content
    private var regPhotoUri: MutableList<String> = mutableListOf()   // capsule registerImage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_capsule)

        val capsuleKey = intent.getStringExtra("capsuleKey")
        var uid = user!!.uid

        // todo: .child(uid)로 수정
        myRef.child("asdfifeiofjn1233").child(capsuleKey.toString()).get().addOnSuccessListener {
            // capsule 별 date, title, detectImage 저장
            date = it.child("date").getValue<String>().toString()
            title = it.child("title").getValue<String>().toString()
            photoUri = it.child("detectImage").getValue<String>()!!.toUri()
            capsuleContent = it.child("content").getValue<String>().toString()
            it.child("registerImage").children.forEach {
                regPhotoUri.add(it.getValue().toString())
            }

            CapsuleDataList.add(ShowCapsuleData(title, date, photoUri, regPhotoUri, capsuleContent))
            initRecycler(uid)
        }
    }

    private fun initRecycler(uid: String) {
        // adaper 와 recyclerView 연결
        val recyclerView: RecyclerView by lazy {
            findViewById(R.id.CapsuleShow)
        }
        // TODO: uid 전달로 변경
        val capsuleAdapter = ShowCapsuleAdapter(this, CapsuleDataList)
        recyclerView.adapter = capsuleAdapter
    }
}