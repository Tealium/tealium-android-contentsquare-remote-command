package com.tealium.remotecommands.contentsquare

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.json.JSONArray
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
    var contentsquareRemoteCommand: ContentsquareRemoteCommand = ContentsquareRemoteCommand(mockk())

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
    fun sendScreenViewWithCustomVarsCalledWithKeys() {
        // Use new JSON mapping format (object of arrays) - same as iOS
        val customVarsObject = JSONObject()
        customVarsObject.put(CustomVars.INDEXES, JSONArray().put(1))
        customVarsObject.put(CustomVars.NAMES, JSONArray().put("category"))
        customVarsObject.put(CustomVars.VALUES, JSONArray().put("electronics"))
        
        val payload = JSONObject()
        payload.put(ScreenView.NAME, "testScreen")
        payload.put(CustomVars.CUSTOM_VARS, customVarsObject)
        
        every { mockCommand.send(any(), any()) } just Runs
        
        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_SCREEN_VIEW), payload)
        
        verify {
            mockCommand.send("testScreen", match { vars -> 
                vars.size == 1 && vars[0].has("index") 
            })
        }
        confirmVerified(mockCommand)
    }
    
    @Test
    fun sendScreenViewWithCustomVarsNotCalledWithoutScreenName() {
        // Use new JSON mapping format (object of arrays) - same as iOS
        val customVarsObject = JSONObject()
        customVarsObject.put(CustomVars.INDEXES, JSONArray().put(1))
        customVarsObject.put(CustomVars.NAMES, JSONArray().put("category"))
        customVarsObject.put(CustomVars.VALUES, JSONArray().put("electronics"))
        
        val payload = JSONObject()
        payload.put(CustomVars.CUSTOM_VARS, customVarsObject)
        
        every { mockCommand.send(any(), any()) } just Runs
        
        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_SCREEN_VIEW), payload)
        
        verify {
            mockCommand wasNot Called
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendScreenViewWithMultipleCustomVarsCalledWithKeys() {
        // Test multiple custom vars using JSON mapping format
        val customVarsObject = JSONObject()
        customVarsObject.put(CustomVars.INDEXES, JSONArray().put(1).put(2))
        customVarsObject.put(CustomVars.NAMES, JSONArray().put("category").put("user_type"))
        customVarsObject.put(CustomVars.VALUES, JSONArray().put("electronics").put("premium"))
        
        val payload = JSONObject()
        payload.put(ScreenView.NAME, "testScreen")
        payload.put(CustomVars.CUSTOM_VARS, customVarsObject)
        
        every { mockCommand.send(any(), any()) } just Runs
        
        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_SCREEN_VIEW), payload)
        
        verify {
            mockCommand.send("testScreen", match { vars -> 
                vars.size == 2 && 
                vars[0].has("index") && vars[0].has("name") && vars[0].has("value") &&
                vars[1].has("index") && vars[1].has("name") && vars[1].has("value")
            })
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

    @Test
    fun sendUserIdentifierCalledWithKey() {
        val payload = JSONObject()
        payload.put(UserIdentifier.USER_IDENTIFIER, "user123")

        every { mockCommand.sendUserIdentifier("user123") } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_USER_IDENTIFIER), payload)

        verify {
            mockCommand.sendUserIdentifier("user123")
        }
        confirmVerified(mockCommand)
    }

    @Test
    fun sendUserIdentifierNotCalledWithoutKey() {
        val payload = JSONObject()
        payload.put("not_user_identifier", "user123")

        every { mockCommand.sendUserIdentifier(any()) } just Runs

        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.SEND_USER_IDENTIFIER), payload)

        verify {
            mockCommand wasNot Called
        }
        confirmVerified(mockCommand)
    }
    
    @Test
    fun stopTrackingCalled() {
        val payload = JSONObject()
        
        every { mockCommand.stopTracking() } just Runs
        
        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.STOP_TRACKING), payload)
        
        verify {
            mockCommand.stopTracking()
        }
        confirmVerified(mockCommand)
    }
    
    @Test
    fun resumeTrackingCalled() {
        val payload = JSONObject()
        
        every { mockCommand.resumeTracking() } just Runs
        
        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.RESUME_TRACKING), payload)
        
        verify {
            mockCommand.resumeTracking()
        }
        confirmVerified(mockCommand)
    }
    
    @Test
    fun forgetMeCalled() {
        val payload = JSONObject()
        
        every { mockCommand.forgetMe() } just Runs
        
        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.FORGET_ME), payload)
        
        verify {
            mockCommand.forgetMe()
        }
        confirmVerified(mockCommand)
    }
    
    @Test
    fun optInCalled() {
        val payload = JSONObject()
        
        every { mockCommand.optIn() } just Runs
        
        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.OPT_IN), payload)
        
        verify {
            mockCommand.optIn()
        }
        confirmVerified(mockCommand)
    }
    
    @Test
    fun optOutCalled() {
        val payload = JSONObject()
        
        every { mockCommand.optOut() } just Runs
        
        contentsquareRemoteCommand.parseCommands(arrayOf(Commands.OPT_OUT), payload)
        
        verify {
            mockCommand.optOut()
        }
        confirmVerified(mockCommand)
    }
}
