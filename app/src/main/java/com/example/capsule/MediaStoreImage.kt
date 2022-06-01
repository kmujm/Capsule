package com.example.capsule

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import java.util.*

data class MediaStoreImage(
    val id: Long,
    val displayName: String,
    val dateTaken: Date,
    val contentUri: Uri
)