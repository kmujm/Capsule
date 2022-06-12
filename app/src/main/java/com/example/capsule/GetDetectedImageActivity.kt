package com.example.capsule

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*
import java.util.jar.Manifest

class GetDetectedImageActivity : Activity() {
    // keyword to detect
    private val keyword : String by lazy{
        intent.getStringExtra(ObjectDetectionActivity.INTENT_KEY_SELECTED_CATAGORY).toString()
    }

    //사용자가 찍은 사진의 uri를 받아옴
    private val mainImageUri : Uri by lazy{
        intent.getParcelableExtra(ObjectDetectionActivity.INTENT_KEY_MAIN_IMAGE)!!
    }

    companion object {
        private const val READ_EXTERNAL_STORAGE_REQUEST = 0x1045
        const val TAG = "GetDetectedImgActivity"
    }

    private val images = mutableListOf<MediaStoreImage>()
    private val imagesDetectedList = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            ActivityCompat.requestPermissions(this, permission, READ_EXTERNAL_STORAGE_REQUEST)
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
            READ_EXTERNAL_STORAGE_REQUEST -> {
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

    private fun showImages() {
        GlobalScope.launch {
            val imageList = queryImages()
            // 갤러리 잘 가져와지나 확인용 로그
            Log.d(TAG, imageList.toString())
            for (image in imageList) {
                runObjectDetection(image, image.contentUri)
            }
            passUri()
        }
    }

    private fun passUri() {
        val intent = Intent(this, ImageSelectActivity::class.java).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putStringArrayListExtra("imageList", ArrayList(imagesDetectedList))
        intent.putExtra("mainImage", mainImageUri)
        startActivity(intent)
        this.finish()
    }

    private fun runObjectDetection(data: MediaStoreImage, imgUri: Uri) {
        lateinit var image: InputImage
        try {
            image = InputImage.fromFilePath(baseContext, imgUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        val objectDetector = ObjectDetection.getClient(options)
        objectDetector.process(image).addOnSuccessListener { results ->
            makeImageList(data, results)
        }
    }

    private fun makeImageList(data: MediaStoreImage, detectedObjects: List<DetectedObject>) {
        detectedObjects.forEachIndexed { index, detectedObject ->
            Log.d(TAG, "INFO : $data")
            Log.d(TAG, "Detected object : $index")
            detectedObject.labels.forEach {
                if (it.text == keyword) {
                    imagesDetectedList.add(data.contentUri.toString())
                }
                Log.d(TAG, " category: ${it.text}")
                Log.d(TAG, " confidence: ${it.confidence}")
                Log.d(TAG, "image list which contain key obejct : ${imagesDetectedList}")
            }
        }
    }

    private suspend fun queryImages(): List<MediaStoreImage> {
        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateTakenColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val dateTaken = Date(cursor.getLong(dateTakenColumn))
                    val displayName = cursor.getString(displayNameColumn)
                    val contentUri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )
                    val image = MediaStoreImage(id, displayName, dateTaken, contentUri)
                    images += image
                }
            }
        }
        return images
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
}