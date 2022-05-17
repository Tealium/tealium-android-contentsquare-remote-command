@file:JvmName("ContentsquareConstants")

package com.tealium.remotecommands.contentsquare

object Commands {
    const val COMMAND_KEY = "command_name"
    const val SEPARATOR = ","

    const val SEND_SCREEN_VIEW = "sendscreenview"
    const val SEND_TRANSACTION = "sendtransaction"
    const val SEND_DYNAMIC_VAR = "senddynamicvar"
    const val STOP_TRACKING = "stoptracking"
    const val RESUME_TRACKING = "resumetracking"
    const val FORGET_ME = "forgetme"
    const val OPT_IN = "optin"
    const val OPT_OUT = "optout"
}

object ScreenView {
    const val NAME = "screen_name"
}

object TransactionProperties {
    const val TRANSACTION = "transaction"
    const val PURCHASE = "purchase"
    const val PRICE = "price" // required
    const val CURRENCY = "currency" // required
    const val ID = "transaction_id" // optional
}

object DynamicVar {
    const val DYNAMIC_VAR = "dynamic_var"
}