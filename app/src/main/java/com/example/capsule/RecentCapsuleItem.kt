package com.example.capsule

import android.net.Uri
import java.util.*

data class RecentCapsuleItem (
    val capsuleDate : Date,
    val capsuleTitle : String,
    val pictureList : MutableList<Uri>
    )