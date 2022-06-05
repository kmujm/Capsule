package com.example.capsule

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class SelectPicActivity : AppCompatActivity() {

    companion object {
        const val READ_EXTERNAL_STORAGE_REQUEST = 0x1045
        const val TAG = "SelectPicActivity"
    }
    private lateinit var mAdapter: SelectPicAdapter
    private lateinit var recyclerView: RecyclerView

    var Images = mutableListOf<ImageData>()
    var selectedviewList = arrayListOf<View>()
    var selectedimageUrlList = arrayListOf<String>() //선택된 이미지 리스트
    var selectCount = 0 //오른쪽 상단에 선택된 이미지의 개수
    var uriArr = arrayListOf<String>() // 갤러리에 있는 모든 사진 uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pic)
        recyclerView = findViewById(R.id.rv_gallery)
        openMediaStore()
    }

    private fun openMediaStore() {
        if (haveStoragePermission()) {
            showImages()
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(
                this, permission,
                GetDetectedImageActivity.READ_EXTERNAL_STORAGE_REQUEST
            )
        }
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GetDetectedImageActivity.READ_EXTERNAL_STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImages()
                } else {
                    val showRational = ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    )
                    if (!showRational) {
                        goToSettings()
                    }
                }
                return
            }
        }
    }

    private fun goToSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            startActivity(intent)
        }
    }

    private fun showImages() {
        CoroutineScope(Dispatchers.Main).launch {
            Images = queryImages()
            Log.d(TAG, "엿머거라 ${Images}")
            Log.d(TAG, "뭐가 더 먼저 된나ㅣ$Images")
            mAdapter = SelectPicAdapter(this, Images)
            initView()
        }
    }

    private fun initView() {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    private suspend fun queryImages(): MutableList<ImageData> {
        var ImageList = mutableListOf<ImageData>()

        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
            )
            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null, // selection
                null, // selectionArgs
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )

                    ImageList.add(ImageData(contentUri.toString(), false))
                }
            }
        }
        return ImageList
    }

    private fun pickImages() {
//        mAdapter.setOnItemClickListener(object : SelectPicAdapter.onItemClickListener {
//            override fun onItemClick(position: Int, holder: ImageView) {
//                Images[position].selected = !Images[position].selected
//            }


//        })
    }

}


