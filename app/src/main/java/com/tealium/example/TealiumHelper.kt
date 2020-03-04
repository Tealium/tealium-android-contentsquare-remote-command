package com.tealium.example

import com.tealium.library.Tealium

object TealiumHelper {

    val instanceName = "my_instance"

    fun trackView(viewName: String) {
        Tealium.getInstance(instanceName)?.trackView(viewName, null)
    }

    fun trackView(viewName: String, data: Map<String, Any>?) {
        Tealium.getInstance(instanceName)?.trackView(viewName, data)
    }

    fun trackEvent(eventName: String) {
        Tealium.getInstance(instanceName)?.trackEvent(eventName, null)
    }

    fun trackEvent(eventName: String, data: Map<String, Any>?) {
        Tealium.getInstance(instanceName)?.trackEvent(eventName, data)
    }
}