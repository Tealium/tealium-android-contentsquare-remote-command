package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tealium.example.databinding.ActivityMiscellaneousBinding

class MiscellaneousActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMiscellaneousBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiscellaneousBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = getString(R.string.title_miscellaneous)
        applyContentInsets(view)

        binding.stopTrackingButton.setOnClickListener {
            TealiumHelper.trackEvent("stop_tracking")
        }

        binding.resumeTrackingButton.setOnClickListener {
            TealiumHelper.trackEvent("resume_tracking")
        }

        binding.optInButton.setOnClickListener {
            TealiumHelper.trackEvent("opt_in")
        }

        binding.optOutButton.setOnClickListener {
            TealiumHelper.trackEvent("opt_out")
        }
    }
}
