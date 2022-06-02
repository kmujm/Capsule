package com.example.capsule

import android.graphics.Bitmap
import com.google.mlkit.common.MlKitException
import java.nio.ByteBuffer


/** An interface to process the images with different vision detectors and custom image models.  */
interface VisionImageProcessor {
    /** Processes a bitmap image.  */
    fun processBitmap(bitmap: Bitmap?, graphicOverlay: GraphicOverlay?)

    /** Stops the underlying machine learning model and release resources.  */
    fun stop()
}