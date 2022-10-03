package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ScreenViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_view)
        TealiumHelper.trackEvent("screen_title", mapOf("screen" to "Another View"))
    }
}
