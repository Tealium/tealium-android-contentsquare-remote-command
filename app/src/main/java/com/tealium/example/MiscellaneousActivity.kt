package com.tealium.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_miscellaneous.*

class MiscellaneousActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_miscellaneous)

        stopTrackingButton.setOnClickListener {
            TealiumHelper.trackEvent("stop_tracking")
        }

        resumeTrackingButton.setOnClickListener {
            TealiumHelper.trackEvent("resume_tracking")
        }

        forgetMeButton.setOnClickListener {
            TealiumHelper.trackEvent("forget_me")
        }

        optInButton.setOnClickListener {
            TealiumHelper.trackEvent("opt_in")
        }

        optOutButton.setOnClickListener {
            TealiumHelper.trackEvent("opt_out")
        }
    }
}
