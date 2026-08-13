package com.tealium.remotecommands.contentsquare

import android.app.Application
import android.util.Log
import com.tealium.remotecommands.RemoteCommand
import org.json.JSONObject
import java.util.*
import kotlin.jvm.Throws

open class ContentsquareRemoteCommand @JvmOverloads constructor(
    commandId: String = DEFAULT_COMMAND_ID,
    description: String = DEFAULT_COMMAND_DESCRIPTION
) : RemoteCommand(commandId, description, BuildConfig.TEALIUM_CONTENTSQUARE_VERSION) {

    @Deprecated(
        message = "The application parameter is no longer required and will be removed in a future version.",
        replaceWith = ReplaceWith("ContentsquareRemoteCommand()")
    )
    constructor(application: Application) : this()

    @Deprecated(
        message = "The application parameter is no longer required and will be removed in a future version.",
        replaceWith = ReplaceWith("ContentsquareRemoteCommand(commandId)")
    )
    constructor(application: Application, commandId: String) : this(commandId)

    @Deprecated(
        message = "The application parameter is no longer required and will be removed in a future version.",
        replaceWith = ReplaceWith("ContentsquareRemoteCommand(commandId, description)")
    )
    constructor(application: Application, commandId: String, description: String) : this(commandId, description)

    private val TAG = this::class.java.simpleName
    var contentsquareInstance: ContentsquareCommand = ContentsquareInstance()

    companion object {
        const val DEFAULT_COMMAND_ID = "contentsquare"
        const val DEFAULT_COMMAND_DESCRIPTION = "Contentsquare-Remote Command"
        const val REQUIRED_KEY = "key does not exist in the payload."
    }

    @Throws(Exception::class)
    override fun onInvoke(response: Response) {
        val payload = response.requestPayload
        val commands = splitCommands(payload)
        parseCommands(commands, payload)
    }

    private fun splitCommands(payload: JSONObject): Array<String> {
        val command = payload.optString(Commands.COMMAND_KEY)
        return command.split(Commands.SEPARATOR.toRegex())
            .dropLastWhile {
                it.isEmpty()
            }
            .map {
                it.trim().lowercase(Locale.ROOT)
            }
            .toTypedArray()
    }

    fun parseCommands(commands: Array<String>, payload: JSONObject) {
        commands.forEach { command ->
            when (command) {
                Commands.SEND_SCREEN_VIEW -> {
                    val screenName = payload.optString(ScreenView.NAME)
                    
                    if (screenName.isNotEmpty()) {
                        // Handle JSON mapping format (object of arrays) - same as iOS
                        val customVarsObject = payload.optJSONObject(CustomVars.CUSTOM_VARS)
                        val customVars = customVarsObject?.let { 
                            customVarsFromArrays(it)
                        }
                        
                        if (customVars != null && customVars.isNotEmpty()) {
                            Log.d(TAG, "Sending screenview $screenName with custom vars")
                            contentsquareInstance.send(screenName, customVars)
                        } else {
                            Log.d(TAG, "Sending screenview $screenName")
                            contentsquareInstance.send(screenName)
                        }
                    } else {
                        Log.e(TAG, "${ScreenView.NAME} $REQUIRED_KEY")
                    }
                }
                Commands.SEND_TRANSACTION -> {
                    var transaction = payload.optJSONObject(TransactionProperties.TRANSACTION)
                    if (transaction == null) {
                        transaction = payload.optJSONObject(TransactionProperties.PURCHASE)
                    }
                    transaction?.let { json ->
                        val amount = json.optDouble(TransactionProperties.PRICE).toFloat()
                        val currency = transaction.optString(TransactionProperties.CURRENCY)
                        var id: String? = transaction.optString(TransactionProperties.ID)
                        id?.let {
                            if (it.isEmpty()) {
                                id = null
                            }
                        }
                        if (amount > 0 && currency.isNotEmpty()) {
                            contentsquareInstance.sendTransaction(amount, currency, id)
                        }
                    } ?: run {
                        Log.e(TAG, "${TransactionProperties.TRANSACTION} or ${TransactionProperties.PURCHASE} $REQUIRED_KEY")
                    }
                }
                Commands.SEND_DYNAMIC_VAR -> {
                    val dynamicVar = payload.optJSONObject(DynamicVar.DYNAMIC_VAR)
                    dynamicVar?.let {
                        contentsquareInstance.sendDynamicVar(dynamicVar)
                    } ?: run {
                        Log.e(TAG, "${DynamicVar.DYNAMIC_VAR} $REQUIRED_KEY")
                    }
                }
                Commands.SEND_USER_IDENTIFIER -> {
                    val userIdentifier = payload.optString(UserIdentifier.USER_IDENTIFIER)
                    if (userIdentifier.isNotEmpty()) {
                        contentsquareInstance.sendUserIdentifier(userIdentifier)
                    } else {
                        Log.e(TAG, "${UserIdentifier.USER_IDENTIFIER} $REQUIRED_KEY")
                    }
                }
                Commands.STOP_TRACKING -> {
                    contentsquareInstance.stopTracking()
                }
                Commands.RESUME_TRACKING -> {
                    contentsquareInstance.resumeTracking()
                }
                Commands.OPT_IN -> {
                    contentsquareInstance.optIn()
                }
                Commands.OPT_OUT -> {
                    contentsquareInstance.optOut()
                }
            }
        }
    }

    private fun customVarsFromArrays(customVarsObject: JSONObject): Array<JSONObject> {
        val indexes = customVarsObject.optJSONArray(CustomVars.INDEXES)
        val names = customVarsObject.optJSONArray(CustomVars.NAMES)
        val values = customVarsObject.optJSONArray(CustomVars.VALUES)
        
        val count = indexes?.length() ?: 0
        val customVars = mutableListOf<JSONObject>()
        
        for (i in 0 until count) {
            val customVar = JSONObject()
            
            if (indexes != null && i < indexes.length()) {
                customVar.put("index", indexes.opt(i))
            }
            if (names != null && i < names.length()) {
                customVar.put("name", names.opt(i))
            }
            if (values != null && i < values.length()) {
                customVar.put("value", values.opt(i))
            }
            
            customVars.add(customVar)
        }
        
        return customVars.toTypedArray()
    }
}
