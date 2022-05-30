package com.example.capsule

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import com.example.capsule.GraphicOverlay
import com.example.capsule.GraphicOverlay.Graphic
import com.google.mlkit.vision.objects.DetectedObject
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/** graphicOverlay 위에 인식된 물체 정보 표시  */
class ObjectGraphic constructor(
    overlay: GraphicOverlay,
    private val detectedObject: DetectedObject
) : GraphicOverlay.Graphic(overlay) {

    private val numColors = COLORS.size

    private val boxPaints = Array(numColors) { Paint() }
    private val textPaints = Array(numColors) { Paint() }
    private val labelPaints = Array(numColors) { Paint() }

    init {
        for (i in 0 until numColors) {
            textPaints[i] = Paint()
            textPaints[i].color = COLORS[i][0]
            textPaints[i].textSize = TEXT_SIZE
            boxPaints[i] = Paint()
            boxPaints[i].color = COLORS[i][1]
            boxPaints[i].style = Paint.Style.STROKE
            boxPaints[i].strokeWidth = STROKE_WIDTH
            labelPaints[i] = Paint()
            labelPaints[i].color = COLORS[i][1]
            labelPaints[i].style = Paint.Style.FILL
        }
    }

    override fun draw(canvas: Canvas) {
        val colorID =
            if (detectedObject.trackingId == null) 0
            else abs(detectedObject.trackingId!! % NUM_COLORS)
        var textWidth =
            textPaints[colorID].measureText("Tracking ID: " + detectedObject.trackingId)
        val lineHeight = TEXT_SIZE + STROKE_WIDTH
        var yLabelOffset = -lineHeight

        // 라벨 박스의 높이와 너비 계산
        for (label in detectedObject.labels) {
            textWidth =
                max(textWidth, textPaints[colorID].measureText(label.text))
            textWidth = max(
                textWidth,
                textPaints[colorID].measureText(
                    String.format(
                        Locale.US,
                        LABEL_FORMAT,
                        label.confidence * 100
                    )
                )
            )
            yLabelOffset -= 2 * lineHeight
        }

        // 박스 그리기
        val rect = RectF(detectedObject.boundingBox)
        val x0 = translateX(rect.left)
        val x1 = translateX(rect.right)
        rect.left = min(x0, x1)
        rect.right = max(x0, x1)
        rect.top = translateY(rect.top)
        rect.bottom = translateY(rect.bottom)
        canvas.drawRect(rect, boxPaints[colorID])

        // 물체 정보 표시
        canvas.drawRect(
            rect.left - STROKE_WIDTH,
            rect.top + yLabelOffset,
            rect.left + textWidth + 2 * STROKE_WIDTH,
            rect.top,
            labelPaints[colorID]
        )
        yLabelOffset += TEXT_SIZE
        /*
        canvas.drawText(
            "Tracking ID: " + detectedObject.trackingId,
            rect.left,
            rect.top + yLabelOffset,
            textPaints[colorID]
        )
        yLabelOffset += lineHeight
        */
        var category : String = ""
        when(detectedObject.labels[0].text){
            "Fashion good"-> category = "패션"
            "Food" -> category = "음식"
            "Home good" -> category = "리빙"
            "place" -> category = "장소"
            "plant" -> category = "식물"
        }

        canvas.drawText(
            category,
            rect.left,
            rect.top + yLabelOffset,
            textPaints[colorID]
        )
        /*
        yLabelOffset += lineHeight
        for (label in detectedObject.labels) {
            canvas.drawText(
                label.text,
                rect.left,
                rect.top + yLabelOffset,
                textPaints[colorID]
            )
            yLabelOffset += lineHeight
            canvas.drawText(
                String.format(
                    Locale.US,
                    LABEL_FORMAT,
                    label.confidence * 100
                ),
                rect.left,
                rect.top + yLabelOffset,
                textPaints[colorID]
            )
            yLabelOffset += lineHeight
        */

        Log.d("draw detected OBJ info", detectedObject.labels[0].text)

    }

    companion object {
        private const val TEXT_SIZE = 54.0f
        private const val STROKE_WIDTH = 4.0f
        private const val NUM_COLORS = 10
        private val COLORS =
            arrayOf(
                intArrayOf(Color.BLACK, Color.WHITE),
                intArrayOf(Color.WHITE, Color.MAGENTA),
                intArrayOf(Color.BLACK, Color.LTGRAY),
                intArrayOf(Color.WHITE, Color.RED),
                intArrayOf(Color.WHITE, Color.BLUE),
                intArrayOf(Color.WHITE, Color.DKGRAY),
                intArrayOf(Color.BLACK, Color.CYAN),
                intArrayOf(Color.BLACK, Color.YELLOW),
                intArrayOf(Color.WHITE, Color.BLACK),
                intArrayOf(Color.BLACK, Color.GREEN)
            )
        private const val LABEL_FORMAT = "confidence : %.2f%%"
    }
}