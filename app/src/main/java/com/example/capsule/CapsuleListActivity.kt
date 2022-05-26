package com.example.capsule

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue


// todo: adapter와 recyclerview 연결
class CapsuleListActivity : AppCompatActivity() {
    private var CapsuleList = ArrayList<CapsuleData>()

    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val myRef: DatabaseReference = mDatabase.getReference("Users")

    var user = FirebaseAuth.getInstance().currentUser

    lateinit var date:String
    lateinit var title:String
    lateinit var photoUri: Uri
    private var capsuleKey: MutableList<String> = mutableListOf()   //capsule key 들만 모아놓은 배열


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capsule_list)

            if (user != null) {
                var uid = user!!.uid
                myRef.child("asdfifeiofjn1233").child("capsuleKey").get().addOnSuccessListener {
                    // capsule key 만 모아놓은 리스트 생성
                    capsuleKey =
                        it.value.toString().replace("[", "").replace("]", "").replace(" ", "")
                            .split(",") as MutableList<String>
                    capsuleKey.removeAt(0)    // 맨 앞 null delete
                    initValue()
                }
                // todo:  .child(uid)로 수정
            }
        }


    private fun initValue() {
        var cnt = 0
        for (capsule in capsuleKey) {
            myRef.child("asdfifeiofjn1233").child(capsule).get().addOnSuccessListener {
                // capsule 별 date, title, detectImage 저장
                date = it.child("date").getValue<String>().toString()
                title = it.child("title").getValue<String>().toString()
                photoUri = it.child("detectImage").getValue<String>()!!.toUri()

                cnt+=1
                CapsuleList.add(CapsuleData(title,date,photoUri))
                Log.i("capsuleTest", CapsuleList.toString())

                if (cnt == capsuleKey.size) {
                    Log.i("capsuleValue", CapsuleList.toString())
                    initRecycler()
                }
            }
        }
    }

    private fun initRecycler() {
        val recyclerView: RecyclerView by lazy {
            findViewById(R.id.CapsuleList)
        }
        val capsuleAdapter = CapsuleDataAdapter(CapsuleList)
        recyclerView.adapter = capsuleAdapter

        Log.i("capsuleInit", CapsuleList.toString())
    }
}