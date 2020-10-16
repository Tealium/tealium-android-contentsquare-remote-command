package com.tealium.remotecommands.contentsquare

import Commands
import DynamicVar
import ScreenView
import TransactionProperties
import android.app.Application
import android.util.Log
import com.tealium.remotecommands.RemoteCommand
import org.json.JSONObject

open class ContentsquareRemoteCommand @JvmOverloads constructor(
    application: Application? = null,
    commandId: String = DEFAULT_COMMAND_ID,
    description: String = DEFAULT_COMMAND_DESCRIPTION
) : RemoteCommand(commandId, description) {

    private val TAG = this::class.java.simpleName
    var contentsquareInstance: ContentsquareCommand = ContentsquareInstance(application)

    companion object {
        val DEFAULT_COMMAND_ID = "contentsquare"
        val DEFAULT_COMMAND_DESCRIPTION = "Contentsquare-Remote Command"
        val REQUIRED_KEY = "key does not exist in the payload."
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
                it.trim().toLowerCase()
            }
            .toTypedArray()
    }

    fun parseCommands(commands: Array<String>, payload: JSONObject) {
        commands.forEach { command ->
            when (command) {
                Commands.SEND_SCREEN_VIEW -> {
                    payload.optString(ScreenView.NAME).also {
                        if (it.isNotEmpty()) {
                            Log.d(TAG, "Sending screenview $it")
                            contentsquareInstance.send(it)
                        } else {
                            Log.d(TAG, "Not sending screenview, ${ScreenView.NAME} was empty")
                        }
                    } ?: run {
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
                Commands.STOP_TRACKING -> {
                    contentsquareInstance.stopTracking()
                }
                Commands.RESUME_TRACKING -> {
                    contentsquareInstance.resumeTracking()
                }
                Commands.FORGET_ME -> {
                    contentsquareInstance.forgetMe()
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
}
