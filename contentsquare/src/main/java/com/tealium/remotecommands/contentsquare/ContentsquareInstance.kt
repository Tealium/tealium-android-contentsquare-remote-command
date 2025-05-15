package com.tealium.remotecommands.contentsquare

import android.app.Application
import android.util.Log
import android.view.View
import com.contentsquare.android.Contentsquare
import com.contentsquare.android.api.Currencies
import com.contentsquare.android.api.model.CustomVar
import com.contentsquare.android.api.model.Transaction
import org.json.JSONObject
import java.util.*

class ContentsquareInstance(private val application: Application) : ContentsquareCommand {

    private val TAG = this::class.java.simpleName

    override fun send(screenName: String) {
        Contentsquare.send(screenName)
    }

    override fun sendTransaction(amount: Float, currency: String, id: String?) {
        Log.d(
            TAG,
            "Sending ${TransactionProperties.TRANSACTION} with ${TransactionProperties.PRICE}: $amount, ${TransactionProperties.CURRENCY}: $currency, ${TransactionProperties.ID}: $id"
        )
        val csCurrency = Currencies.fromString(currency.uppercase(Locale.ROOT))
        id?.let {
            Contentsquare.send(Transaction.builder(amount, csCurrency).id(it).build())
        } ?: run {
            Contentsquare.send(Transaction.builder(amount, csCurrency).build())
        }
    }

    override fun sendDynamicVar(dynamicVar: JSONObject) {
        Log.d(TAG, "${DynamicVar.DYNAMIC_VAR}: $dynamicVar")
        dynamicVar.keys().forEach { key ->
            val value = dynamicVar.optString(key)
            if (value.isNotEmpty()) {
                Log.d(TAG, "Sending ${DynamicVar.DYNAMIC_VAR} with key: $key, value: $value")
                Contentsquare.send(key, value)
            } else {
                Log.d(TAG, "Not sending ${DynamicVar.DYNAMIC_VAR}, $value is empty.")
            }
        }
    }

    override fun sendUserIdentifier(userId: String) {
        Log.d(TAG, "Sending user identifier: $userId")
        Contentsquare.sendUserIdentifier(userId)
    }

    override fun sendCustomVars(screenName: String, customVars: Array<JSONObject>) {
        Log.d(TAG, "Sending custom variables for screen: $screenName")
        val csCustomVars = customVars.mapNotNull { json ->
            val index = json.optInt(CustomVars.INDEX, -1)
            val name = json.optString(CustomVars.NAME)
            val value = json.optString(CustomVars.VALUE)
            
            if (index >= 0 && name.isNotEmpty() && value.isNotEmpty()) {
                CustomVar(index, name, value)
            } else {
                Log.e(TAG, "Invalid custom var: $json")
                null
            }
        }.toTypedArray()
        
        if (csCustomVars.isNotEmpty()) {
            Contentsquare.send(screenName, csCustomVars)
        } else {
            Log.d(TAG, "No valid custom vars to send")
        }
    }

    override fun stopTracking() {
        Contentsquare.stopTracking()
    }

    override fun resumeTracking() {
        Contentsquare.resumeTracking()
    }

    override fun forgetMe() {
        Contentsquare.forgetMe()
    }

    override fun optIn() {
        Contentsquare.optIn()
    }

    override fun optOut() {
        Contentsquare.optOut()
    }
}
