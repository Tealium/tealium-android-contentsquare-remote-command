package com.tealium.remotecommands.contentsquare

import TransactionProperties
import ScreenView
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.json.JSONObject
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ContentsquareRemoteCommandTest {

    @MockK
    lateinit var mockTracker: ContentsquareTrackable

    @InjectMockKs
    var contentsquareRemoteCommand: ContentsquareRemoteCommand = ContentsquareRemoteCommand(null)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        contentsquareRemoteCommand.tracker = mockTracker
    }

    @Test
    fun sendScreenViewCalledWithKey() {
        val payload = JSONObject()
        payload.put(ScreenView.NAME, "home")

        every { mockTracker.send("home") } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_SCREEN_VIEW), payload)

        verify {
            mockTracker.send("home")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun sendScreenViewNotCalledWithoutKey() {
        val payload = JSONObject()
        payload.put("not_screen_name", "home")

        every { mockTracker.send("home") } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_SCREEN_VIEW), payload)

        verify {
            mockTracker wasNot Called
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun sendTransactionCalledWithPayloadKey() {
        val innerPayload = JSONObject()
        innerPayload.put(TransactionProperties.PRICE, 10.99)
        innerPayload.put(TransactionProperties.CURRENCY, 1)

        val outerPayload = JSONObject()
        outerPayload.put(TransactionProperties.TRANSACTION, innerPayload)

        every { mockTracker.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockTracker.sendTransaction(10.99F, "usd", null)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun sendTransactionCalledWithIdKey() {
        val innerPayload = JSONObject()
        innerPayload.put(TransactionProperties.PRICE, 10.99)
        innerPayload.put(TransactionProperties.CURRENCY, 1)
        innerPayload.put(TransactionProperties.ID, 123)

        val outerPayload = JSONObject()
        outerPayload.put(TransactionProperties.TRANSACTION, innerPayload)

        every { mockTracker.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockTracker.sendTransaction(10.99F, "eur", "123")
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun sendTransactionNotCalledWithoutPayloadKey() {
        val innerPayload = JSONObject()
        innerPayload.put(TransactionProperties.PRICE, 10.99)
        innerPayload.put(TransactionProperties.CURRENCY, 1)

        val outerPayload = JSONObject()
        outerPayload.put("not_transaction", innerPayload)

        every { mockTracker.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockTracker wasNot Called
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun sendTransactionNotCalledWithoutAmountAndCurrencyKeys() {
        val innerPayload = JSONObject()
        innerPayload.put("not_price", 10.99)
        innerPayload.put("not_currency", 1)

        val outerPayload = JSONObject()
        outerPayload.put(TransactionProperties.TRANSACTION, innerPayload)

        every { mockTracker.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockTracker wasNot Called
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun sendTransactionNotCalledWithoutPositivePriceAndCurrencyKeys() {
        val innerPayload = JSONObject()
        innerPayload.put(TransactionProperties.PRICE, 0)
        innerPayload.put(TransactionProperties.CURRENCY, 0)

        val outerPayload = JSONObject()
        outerPayload.put(TransactionProperties.TRANSACTION, innerPayload)

        every { mockTracker.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockTracker wasNot Called
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun sendDynamicVarCalledWithKey() {
        val innerPayload = JSONObject()
        innerPayload.put("key1", "value1")

        val outerPayload = JSONObject()
        outerPayload.put(DynamicVar.DYNAMIC_VAR, innerPayload)

        every { mockTracker.sendDynamicVar(any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_DYNAMIC_VAR), outerPayload)

        verify {
            mockTracker.sendDynamicVar(innerPayload)
        }
        confirmVerified(mockTracker)
    }

    @Test
    fun sendDynamicVarNotCalledWithoutKey() {
        val innerPayload = JSONObject()
        innerPayload.put("key1", "value1")

        val outerPayload = JSONObject()
        outerPayload.put("not_dynamic_var", innerPayload)

        every { mockTracker.sendDynamicVar(any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_DYNAMIC_VAR), outerPayload)

        verify {
            mockTracker wasNot Called
        }
        confirmVerified(mockTracker)
    }
}
