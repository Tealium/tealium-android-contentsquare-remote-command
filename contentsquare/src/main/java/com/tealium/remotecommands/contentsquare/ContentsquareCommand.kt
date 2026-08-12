package com.tealium.remotecommands.contentsquare

import org.json.JSONObject

interface ContentsquareCommand {
    fun send(screenName: String, customVars: Array<JSONObject>? = null)
    fun sendTransaction(amount: Float, currency: String, id: String? = null)
    fun sendDynamicVar(dynamicVar: JSONObject)
    fun sendUserIdentifier(userId: String)
    fun stopTracking()
    fun resumeTracking()
    fun optIn()
    fun optOut()
}