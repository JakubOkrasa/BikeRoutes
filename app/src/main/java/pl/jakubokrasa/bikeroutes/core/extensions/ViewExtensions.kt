package pl.jakubokrasa.bikeroutes.core.extensions

import android.R
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import com.google.android.material.slider.RangeSlider


fun View.makeVisible() = run { visibility = View.VISIBLE }

fun View.makeGone() = run { visibility = View.GONE }

fun View.isVisible() = visibility == View.VISIBLE

fun RangeSlider.getValFrom(): Int {
    return values[0].toInt()
}

fun RangeSlider.getValTo(): Int {
    return values[1].toInt()
}

fun TextView.displayWithShowMoreButton(maxLines: Int, longText: String) {
    this.post {
        val twoSpaces = "  "

        this.text = longText
        if (this.lineCount > maxLines) {
            val lastCharShown: Int = this.layout.getLineVisibleEnd(maxLines - 1)
            this.maxLines = maxLines
            val moreString = "Show more"
            val suffix = twoSpaces + moreString

            // 3 is the length of the ellipsis we're going to insert
            val actionDisplayText: String =
                longText.substring(0, lastCharShown - suffix.length - 3) + String.format("...  $moreString")
            val truncatedSpannableString = SpannableString(actionDisplayText)
            val startIndex = actionDisplayText.indexOf(moreString)
            truncatedSpannableString.setSpan(ForegroundColorSpan(Color.BLUE),
                startIndex,
                startIndex + moreString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            truncatedSpannableString.setSpan(RelativeSizeSpan(this.textSize),
                startIndex,
                startIndex + moreString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.text = truncatedSpannableString
        }
    }
}