package com.tealium.remotecommands.contentsquare

import android.util.Log
import com.contentsquare.android.Contentsquare
import com.contentsquare.android.api.Currencies
import com.contentsquare.android.api.model.CustomVar
import com.contentsquare.android.api.model.Transaction
import org.json.JSONObject
import java.util.*

class ContentsquareInstance : ContentsquareCommand {

    private val TAG = this::class.java.simpleName

    override fun send(screenName: String, customVars: Array<JSONObject>?) {
        if (!customVars.isNullOrEmpty()) {
            Log.d(TAG, "Sending custom variables for screen: $screenName")
            val csCustomVars = customVars.mapNotNull(::jsonObjectToCustomVar).toTypedArray()
            
            if (csCustomVars.isNotEmpty()) {
                Contentsquare.send(screenName, csCustomVars)
                return
            }
            
            Log.d(TAG, "No valid custom vars to send, sending regular screen view")
        }

        Contentsquare.send(screenName)
    }

    private fun jsonObjectToCustomVar(json: JSONObject): CustomVar? {
        val index = json.optInt("index", -1)
        if (index <= 0) {
            Log.e(TAG, "Invalid custom var index: $index (must be positive). Skipping: $json")
            return null
        }

        val name = json.optString("name")
        val value = json.optString("value")

        return CustomVar(index, name, value)
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

    override fun stopTracking() {
        Contentsquare.stopTracking()
    }

    override fun resumeTracking() {
        Contentsquare.resumeTracking()
    }

    override fun optIn() {
        Contentsquare.optIn()
    }

    override fun optOut() {
        Contentsquare.optOut()
    }
}
