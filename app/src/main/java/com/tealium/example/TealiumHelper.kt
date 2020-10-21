package com.tealium.example

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.tealium.core.Dispatchers
import com.tealium.core.Environment
import com.tealium.core.Tealium
import com.tealium.core.TealiumConfig
import com.tealium.dispatcher.TealiumEvent
import com.tealium.dispatcher.TealiumView
import com.tealium.remotecommanddispatcher.RemoteCommands
import com.tealium.remotecommanddispatcher.remoteCommands
import com.tealium.remotecommands.contentsquare.ContentsquareRemoteCommand
import com.tealium.tealiumlibrary.BuildConfig

object TealiumHelper {

    const val instanceName = "my_instance"
    lateinit var tealium: Tealium

    fun initialize(application: Application) {
        val config = TealiumConfig(
            application,
            "tealiummobile",
            "contentsquare-dev",
            Environment.DEV
        ).apply {

            // TagManagement controlled RemoteCommand
            // dispatchers.add(Dispatchers.TagManagement)

            // JSON controlled RemoteCommand
            dispatchers.add(Dispatchers.RemoteCommands)
        }
        tealium = Tealium.create(instanceName, config) {
            val remoteCommand = ContentsquareRemoteCommand(application)
            tealium.remoteCommands?.add(remoteCommand, "contentsquare.json")
        }
    }

    fun trackView(viewName: String, data: Map<String, Any>? = null) {
        val view = TealiumView(viewName, data)
        tealium.track(view)
    }

    fun trackEvent(eventName: String, data: Map<String, Any>? = null) {
        val event = TealiumEvent(eventName, data)
        tealium.track(event)
    }
}