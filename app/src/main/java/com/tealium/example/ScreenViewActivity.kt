package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ScreenViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_view)
        TealiumHelper.trackEvent("screen_title", mapOf("screen" to "Screen Views"))

        supportActionBar?.title = getString(R.string.title_screen_views)
        applyContentInsets(findViewById(android.R.id.content))

        val screenNameInput = findViewById<EditText>(R.id.apiKeyTextView)
        findViewById<Button>(R.id.trackScreenButton).setOnClickListener {
            val screenName = screenNameInput.text.toString().ifBlank { "Another View" }
            TealiumHelper.trackView("screen_title", mapOf("screen" to screenName))
        }
    }
}
