package com.example.capsule

import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var totalSelectText: TextView
    private lateinit var completeBtn: TextView
    private lateinit var backBtn: ImageView
    private var selectCnt = 0

    var Images = mutableListOf<ImageData>()
    val passData = mutableSetOf<String>()

    private val mainImageUri : Uri by lazy{
        intent.getParcelableExtra("mainImage")!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pic)
        recyclerView = findViewById(R.id.rv_gallery)
        totalSelectText = findViewById(R.id.select_count)
        completeBtn = findViewById(R.id.complete_button)
        backBtn = findViewById(R.id.back_button)
        openMediaStore()
        initCompleteButton()
        initBackBtn()
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
            mAdapter = SelectPicAdapter(this, Images)
            initView()
            pickImages()
        }
    }

    private fun initView() {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    private fun initBackBtn() {
        backBtn.setOnClickListener {
            finish()
        }
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
        mAdapter.setOnItemClickListener(object : SelectPicAdapter.onItemClickListener {
            override fun onItemClick(
                position: Int,
                holder: ImageView,
                frame: ImageView,
                circle: ImageView,
                cnt: TextView
            ) {
                if (Images[position].selected) {
                    selectCnt--
                    totalSelectText.text = selectCnt.toString()
                    Images[position].selected = false
                    frame.visibility = ImageView.INVISIBLE
                    circle.visibility = ImageView.INVISIBLE


                } else {
                    selectCnt++
                    totalSelectText.text = selectCnt.toString()
                    Images[position].selected = true
                    frame.visibility = ImageView.VISIBLE
                    circle.visibility = ImageView.VISIBLE
                }
            }
        })
    }

    private fun initCompleteButton() {
        completeBtn.setOnClickListener {
            for (item in Images) {
                if (item.selected) {
                    passData.add(item.contentUri)
                }
            }
            Log.d(TAG, "넘겨주는 값 $passData")
            val intent = Intent(this, PostCapsuleActivity::class.java)
            intent.putStringArrayListExtra("passData", ArrayList(passData))
            intent.putExtra("mainImage", mainImageUri.toString())
            startActivity(intent)
            this.finish()
        }

    }


}


