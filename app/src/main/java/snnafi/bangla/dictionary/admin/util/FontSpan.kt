package snnafi.bangla.dictionary.admin.util

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

open class FontSpan(
    private val font: Typeface?,
    private val fontStyle: FontStyle = FontStyle.BOLD
) :
    MetricAffectingSpan() {
    override fun updateMeasureState(textPaint: TextPaint) = updateTypeface(textPaint)

    override fun updateDrawState(textPaint: TextPaint) = updateTypeface(textPaint)

    private fun updateTypeface(textPaint: TextPaint) {
        textPaint.apply {
            typeface = Typeface.create(font, fontStyle.ordinal + 1)
        }
    }

    enum class FontStyle {
        BOLD, ITALIC
        // bold = 1, italic = 2
    }


}