package com.tealium.remotecommands.contentsquare

import TransactionProperties
import DynamicVar
import android.app.Application
import android.util.Log
import com.contentsquare.android.ContentSquare
import com.contentsquare.android.api.model.Transaction
import org.json.JSONObject

class ContentsquareTracker(val application: Application? = null) : ContentsquareTrackable {

    private val TAG = this::class.java.simpleName

    override fun send(screenName: String) {
        ContentSquare.send(screenName)
    }

    override fun sendTransaction(amount: Float, currency: Int, id: String?) {
        Log.d(
            TAG,
            "Sending ${TransactionProperties.TRANSACTION} with ${TransactionProperties.PRICE}: $amount, ${TransactionProperties.CURRENCY}: $currency, ${TransactionProperties.ID}: $id"
        )
        id?.let {
            ContentSquare.send(Transaction.builder(amount, currency).id(id).build())
        } ?: run {
            ContentSquare.send(Transaction.builder(amount, currency).build())
        }
    }

    override fun sendDynamicVar(dynamicVar: JSONObject) {
        Log.d(TAG, "${DynamicVar.DYNAMIC_VAR}: $dynamicVar")
        dynamicVar.keys().forEach { key ->
            val value = dynamicVar.optString(key)
            if (value.isNotEmpty()) {
                Log.d(TAG, "Sending ${DynamicVar.DYNAMIC_VAR} with key: $key, value: $value")
                ContentSquare.send(key, value)
            } else {
                Log.d(TAG, "Not sending ${DynamicVar.DYNAMIC_VAR}, $value is empty.")
            }
        }
    }

    override fun stopTracking() {
        ContentSquare.stopTracking()
    }

    override fun resumeTracking() {
        ContentSquare.resumeTracking()
    }

    override fun forgetMe() {
        ContentSquare.forgetMe()
    }

    override fun optIn() {
        application?.let {
            ContentSquare.optIn(application.applicationContext)
        } ?: run {
            Log.d(
                TAG,
                "Application was null. Initialize ContentsquareRemoteCommand with `application`."
            )
        }
    }

    override fun optOut() {
        application?.let {
            ContentSquare.optOut(application.applicationContext)
        } ?: run {
            Log.d(
                TAG,
                "Application was null. Initialize ContentsquareRemoteCommand with `application`."
            )
        }
    }
}