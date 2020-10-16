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
    lateinit var mockCommand: ContentsquareCommand

    @InjectMockKs
    var contentsquareRemoteCommand: ContentsquareRemoteCommand = ContentsquareRemoteCommand(null)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        contentsquareRemoteCommand.contentsquareInstance = mockCommand
    }

    @Test
    fun sendScreenViewCalledWithKey() {
        val payload = JSONObject()
        payload.put(ScreenView.NAME, "home")

        every { mockCommand.send("home") } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_SCREEN_VIEW), payload)

        verify {
            mockCommand.send("home")
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendScreenViewNotCalledWithoutKey() {
        val payload = JSONObject()
        payload.put("not_screen_name", "home")

        every { mockCommand.send("home") } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_SCREEN_VIEW), payload)

        verify {
            mockCommand wasNot Called
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendTransactionCalledWithPayloadKey() {
        val innerPayload = JSONObject()
        innerPayload.put(TransactionProperties.PRICE, 10.99)
        innerPayload.put(TransactionProperties.CURRENCY, "1")

        val outerPayload = JSONObject()
        outerPayload.put(TransactionProperties.TRANSACTION, innerPayload)

        every { mockCommand.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockCommand.sendTransaction(10.99F, "1", null)
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendTransactionCalledWithIdKey() {
        val innerPayload = JSONObject()
        innerPayload.put(TransactionProperties.PRICE, 10.99)
        innerPayload.put(TransactionProperties.CURRENCY, 1)
        innerPayload.put(TransactionProperties.ID, 123)

        val outerPayload = JSONObject()
        outerPayload.put(TransactionProperties.TRANSACTION, innerPayload)

        every { mockCommand.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockCommand.sendTransaction(10.99F, "1", "123")
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendTransactionNotCalledWithoutPayloadKey() {
        val innerPayload = JSONObject()
        innerPayload.put(TransactionProperties.PRICE, 10.99)
        innerPayload.put(TransactionProperties.CURRENCY, 1)

        val outerPayload = JSONObject()
        outerPayload.put("not_transaction", innerPayload)

        every { mockCommand.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockCommand wasNot Called
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendTransactionNotCalledWithoutAmountAndCurrencyKeys() {
        val innerPayload = JSONObject()
        innerPayload.put("not_price", 10.99)
        innerPayload.put("not_currency", 1)

        val outerPayload = JSONObject()
        outerPayload.put(TransactionProperties.TRANSACTION, innerPayload)

        every { mockCommand.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockCommand wasNot Called
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendTransactionNotCalledWithoutPositivePriceAndCurrencyKeys() {
        val innerPayload = JSONObject()
        innerPayload.put(TransactionProperties.PRICE, 0)
        innerPayload.put(TransactionProperties.CURRENCY, 0)

        val outerPayload = JSONObject()
        outerPayload.put(TransactionProperties.TRANSACTION, innerPayload)

        every { mockCommand.sendTransaction(any(), any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_TRANSACTION), outerPayload)

        verify {
            mockCommand wasNot Called
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendDynamicVarCalledWithKey() {
        val innerPayload = JSONObject()
        innerPayload.put("key1", "value1")

        val outerPayload = JSONObject()
        outerPayload.put(DynamicVar.DYNAMIC_VAR, innerPayload)

        every { mockCommand.sendDynamicVar(any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_DYNAMIC_VAR), outerPayload)

        verify {
            mockCommand.sendDynamicVar(innerPayload)
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendDynamicVarNotCalledWithoutKey() {
        val innerPayload = JSONObject()
        innerPayload.put("key1", "value1")

        val outerPayload = JSONObject()
        outerPayload.put("not_dynamic_var", innerPayload)

        every { mockCommand.sendDynamicVar(any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_DYNAMIC_VAR), outerPayload)

        verify {
            mockCommand wasNot Called
        }
        confirmVerified(mockCommand)
    }
}
