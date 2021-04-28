package com.example.androidmtgcompanionkotlin
//https://codelabs.developers.google.com/codelabs/mlkit-android#4
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import com.google.mlkit.vision.text.Text


/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
class TextGraphic internal constructor(overlay: GraphicOverlay?, private val element: Text.Element?) : GraphicOverlay.Graphic(overlay!!) {
    private val rectPaint: Paint
    private val textPaint: Paint

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    override fun draw(canvas: Canvas?) {
   // fun draw(canvas: Canvas) {
        Log.d(TAG, "on draw text graphic")
        checkNotNull(element) { "Attempting to draw a null text." }
        Log.d(TAG, "Text:" + element.text)
        // Draws the bounding box around the TextBlock.
        val rect = RectF(element.boundingBox)
        if (canvas!=null) {
            canvas.drawRect(rect, rectPaint)

            // Renders the text at the bottom of the box.
            canvas.drawText(element.text, rect.left, rect.bottom, textPaint)
        }

    }

    companion object {
        private const val TAG = "TextGraphic"
        private const val TEXT_COLOR = Color.RED
        private const val TEXT_SIZE = 54.0f
        private const val STROKE_WIDTH = 4.0f
    }

    init {
        rectPaint = Paint()
        rectPaint.color = TEXT_COLOR
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = STROKE_WIDTH
        textPaint = Paint()
        textPaint.color = Color.BLUE
        textPaint.textSize = TEXT_SIZE
        // Redraw the overlay, as this graphic has been added.
        postInvalidate()
    }


}
