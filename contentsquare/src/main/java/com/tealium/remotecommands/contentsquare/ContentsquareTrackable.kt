package com.tealium.remotecommands.contentsquare

import org.json.JSONObject

interface ContentsquareTrackable {
    fun send(screenName: String)
    fun sendTransaction(amount: Float, currency: String, id: String? = null)
    fun sendDynamicVar(dynamicVar: JSONObject)
    fun stopTracking()
    fun resumeTracking()
    fun forgetMe()
    fun optIn()
    fun optOut()
}