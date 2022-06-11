package com.example.capsule

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

class ShowCapsuleActivity : AppCompatActivity() {
    private val Ctitle: TextView by lazy {    // capsule title
        findViewById(R.id.CapsuleTitle)
    }
    private val CDate: TextView by lazy {    // capsule date
        findViewById(R.id.CapsuleDate)
    }
    private val CPhoto: ImageView by lazy {    // capsule detect Image
        findViewById(R.id.CapsuleImage)
    }
    private val content: EditText by lazy {    // capsule detect Image
        findViewById(R.id.CapsuleContent)
    }
    private val btnBack: Button by lazy {
        findViewById(R.id.btn_ShowCapsuleBack)
    }

    private var CapsuleDataList = ArrayList<ShowCapsuleData>()    // recyclerView 에 띄워줄 데이터 저장

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")
    private var user = FirebaseAuth.getInstance().currentUser    // 현재 로그인한 유저

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_capsule)

        // key, title, date, photo 받아옴
        val capsuleKey = intent.getStringExtra("capsuleKey")
        val capsuleTitle = intent.getStringExtra("capsuleTitle")
        val capsuleDate = intent.getStringExtra("capsuleDate")
        val capsulePhoto = intent.getStringExtra("capsulePhoto") // capsule detectImage
        val capsuleContent = intent.getStringExtra("capsuleContent")

        var uid = user!!.uid

        // title, date, detect Image 띄워줌
        Ctitle.setText(capsuleTitle.toString())
        CDate.setText(capsuleDate.toString())
        content.setText(capsuleContent.toString())
        Glide.with(this).load(capsulePhoto?.toUri()).into(CPhoto);

        // todo: .child(uid)로 수정
        myRef.child("asdfifeiofjn1233").child(capsuleKey.toString()).get().addOnSuccessListener {
            it.child("registerImage").children.forEach {
                CapsuleDataList.add(ShowCapsuleData(it.getValue().toString().toUri()))
            }
            initRecycler(uid)
        }

        // 뒤로가기 버튼 클릭시 캡슐 목록으로 이동
        btnBack.setOnClickListener {
            val intent = Intent(this, CapsuleListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecycler(uid: String) {
        // adaper 와 recyclerView 연결
        val recyclerView: RecyclerView by lazy {
            findViewById(R.id.CapsuleShow)
        }
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        // TODO: uid 전달로 변경
        val capsuleAdapter = ShowCapsuleAdapter(this, CapsuleDataList)
        recyclerView.adapter = capsuleAdapter
    }
}