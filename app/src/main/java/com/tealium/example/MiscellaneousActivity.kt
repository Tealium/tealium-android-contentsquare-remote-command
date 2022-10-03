package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tealium.example.databinding.ActivityMiscellaneousBinding

class MiscellaneousActivity : AppCompatActivity() {

    lateinit var binding: ActivityMiscellaneousBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiscellaneousBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.stopTrackingButton.setOnClickListener {
            TealiumHelper.trackEvent("stop_tracking")
        }

        binding.resumeTrackingButton.setOnClickListener {
            TealiumHelper.trackEvent("resume_tracking")
        }

        binding.forgetMeButton.setOnClickListener {
            TealiumHelper.trackEvent("forget_me")
        }

        binding.optInButton.setOnClickListener {
            TealiumHelper.trackEvent("opt_in")
        }

        binding.optOutButton.setOnClickListener {
            TealiumHelper.trackEvent("opt_out")
        }
    }
}
