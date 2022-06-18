package com.example.capsule

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock.sleep
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private lateinit var userId : String

    private val recentCapsuleList = mutableListOf<RecentCapsuleItem>() // 최근 등록 캡슐들을 저장하는 리스트

    private val alarmButton : ImageButton by lazy{
        findViewById(R.id.alarm)
    }

    private val mainPageButton : ImageButton by lazy{
        findViewById(R.id.mainPage)
    }

    private val settingButton : ImageButton by lazy{
        findViewById(R.id.settings)
    }

    private val greeting : TextView by lazy{
        findViewById(R.id.greeting)
    }

    private val mAdapter : RecentCapsuleAdapter by lazy{
        RecentCapsuleAdapter(this, recentCapsuleList)
    }

    private var username = "USERNAME"

    private val objectDetectionButton : Button by lazy{
        findViewById(R.id.objectDetectionButton)
    }

    private val CapsuleListButton : Button by lazy{
        findViewById(R.id.capsuleListButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Firebase.database.reference

        initGreeting()
        initObjectDetectionButton()
        initCapsuleListButton()
        initSettingButton()
    }

    override fun onResume() {
        super.onResume()
        getCapsuleKeys()
    }

    private fun initSettingButton(){
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initObjectDetectionButton(){
        objectDetectionButton.setOnClickListener {
            val intent = Intent(this, ObjectDetectionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initCapsuleListButton(){
        CapsuleListButton.setOnClickListener {
            val intent = Intent(this, CapsuleListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userGreeting(){
        val content = "${username} 님,\n오늘도 캡슐하세요!"
        val spannableString = SpannableString(content)
        val start_idx = 0
        val end_idx = username.length

        spannableString.setSpan(StyleSpan(Typeface.BOLD), start_idx, end_idx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        greeting.text = spannableString
    }

    private fun initGreeting(){
        userGreeting()

        val user = Firebase.auth.currentUser
        if(user != null){
            userId = Firebase.auth.currentUser!!.uid
            database.child("Users").child(userId).child("Info").child("nickname").get().addOnSuccessListener {
                username = it.value.toString()
                userGreeting()
                Log.d("firebase", "Success getting user nickname data")
            }.addOnFailureListener {
                Log.d("firebase", "Fail getting user nickname data", it)
            }
        }
    }

    private fun getCapsuleKeys(){
        Log.d("userId", userId)
        val myRef = database.child("Users").child(userId)
        // 각 유저 데이터베이스 밑에 존재하는 캡슐 키를 가져오는 코드
        val capsuleKey = mutableListOf<String>()
        myRef.get().addOnSuccessListener {
            it.children.forEach{
                if(it.key.toString() != "Info" && capsuleKey.size < 5){
                    capsuleKey.add(it.key.toString())
                } else{
                    Log.d("DB", it.key.toString())
                }
            }
            Log.d("capsuleKey", capsuleKey.toString())
            setCapsuleData(capsuleKey)
        }.addOnFailureListener {
            //
        }
    }

    private fun setCapsuleData(capsuleKey : MutableList<String>){
        // 가져온 캡슐 키로 캡슐을 조회하여 recyclerView의 아이템에 세팅
        var cnt = 0
        recentCapsuleList.clear()
        for(key in capsuleKey){
            database.child("Users").child(userId).child(key).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val pictureList = mutableListOf<Uri>()
                    p0.child("registerImage").children.forEach(){
                        pictureList.add(Uri.parse(it.value as String))
                    }
                    val capsule = RecentCapsuleItem(p0.child("date").value.toString(), p0.child("title").value.toString(), pictureList, key, p0.child("detectImage").value.toString(), p0.child("content").value.toString())
                    recentCapsuleList.add(capsule)

                    if(++cnt == capsuleKey.size){
                        initRecyclerView()
                    }
                    mAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("firebase", "캡슐을 가져오는데 실패했습니다.")
                }
            })
        }
    }

    private fun initRecyclerView(){
        val rv_recentCapsule :RecyclerView = findViewById(R.id.rv_recentCapsule)

        rv_recentCapsule.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        rv_recentCapsule.setHasFixedSize(true)

        Log.d("recentCapsuleList", recentCapsuleList.toString())
        rv_recentCapsule.adapter = mAdapter
    }
}