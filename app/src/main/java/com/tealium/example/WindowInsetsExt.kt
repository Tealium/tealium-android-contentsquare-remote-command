package com.tealium.example

import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * Apps targeting Android 15 (SDK 35) are edge-to-edge, so content draws behind the
 * system bars and the action bar unless we offset it ourselves. This pads [view] by
 * the system-bar insets plus the action bar height so content starts below the bar.
 */
fun AppCompatActivity.applyContentInsets(view: View) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { root, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        root.updatePadding(
            top = insets.top + actionBarHeight(),
            bottom = insets.bottom,
            left = insets.left,
            right = insets.right
        )
        WindowInsetsCompat.CONSUMED
    }
}

private fun AppCompatActivity.actionBarHeight(): Int {
    val value = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, value, true)) {
        TypedValue.complexToDimensionPixelSize(value.data, resources.displayMetrics)
    } else {
        0
    }
}
