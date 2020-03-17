package com.tealium.remotecommands.contentsquare

import DynamicVar
import TransactionProperties
import android.app.Application
import android.util.Log
import com.contentsquare.android.ContentSquare
import com.contentsquare.android.api.model.Transaction
import org.json.JSONObject

class ContentsquareTracker(private val application: Application? = null) : ContentsquareTrackable {

    private val TAG = this::class.java.simpleName

    override fun send(screenName: String) {
        ContentSquare.send(screenName)
    }

    override fun sendTransaction(amount: Float, currency: String, id: String?) {
        Log.d(
            TAG,
            "Sending ${TransactionProperties.TRANSACTION} with ${TransactionProperties.PRICE}: $amount, ${TransactionProperties.CURRENCY}: $currency, ${TransactionProperties.ID}: $id"
        )
        val csCurrency = currency(currency)
        id?.let {
            ContentSquare.send(Transaction.builder(amount, csCurrency).id(it).build())
        } ?: run {
            ContentSquare.send(Transaction.builder(amount, csCurrency).build())
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
            ContentSquare.optOut(it.applicationContext)
        } ?: run {
            Log.d(
                TAG,
                "Application was null. Initialize ContentsquareRemoteCommand with `application`."
            )
        }
    }

    private fun currency(value: String): Int {
        val currencyType = CurrencyType.valueOf(value.toUpperCase())
        return currencyType.value
    }

    enum class MyEnum {
        Foo, Bar, Baz
    }

    enum class CurrencyType(val value: Int) {
        AFN(971),
        EUR(978),
        ALL(8),
        DZD(12),
        USD(840),
        AOA(973),
        XCD(951),
        ARS(32),
        AMD(51),
        AWG(533),
        AUD(36),
        AZN(944),
        BSD(44),
        BHD(48),
        BDT(50),
        BBD(52),
        BYN(933),
        BZD(84),
        XOF(952),
        BMD(60),
        INR(356),
        BTN(64),
        BOB(68),
        BOV(984),
        BAM(977),
        BWP(72),
        NOK(578),
        BRL(986),
        BND(96),
        BGN(975),
        BIF(108),
        CVE(132),
        KHR(116),
        XAF(950),
        CAD(124),
        KYD(136),
        CLP(152),
        CLF(990),
        CNY(156),
        COP(170),
        COU(970),
        KMF(174),
        CDF(976),
        NZD(554),
        CRC(188),
        HRK(191),
        CUP(192),
        CUC(931),
        ANG(532),
        CZK(203),
        DKK(208),
        DJF(262),
        DOP(214),
        EGP(818),
        SVC(222),
        ERN(232),
        ETB(230),
        FKP(238),
        FJD(242),
        XPF(953),
        GMD(270),
        GEL(981),
        GHS(936),
        GIP(292),
        GTQ(320),
        GBP(826),
        GNF(324),
        GYD(328),
        HTG(332),
        HNL(340),
        HKD(344),
        HUF(348),
        ISK(352),
        IDR(360),
        XDR(960),
        IRR(364),
        IQD(368),
        ILS(376),
        JMD(388),
        JPY(392),
        JOD(400),
        KZT(398),
        KES(404),
        KPW(408),
        KRW(410),
        KWD(414),
        KGS(417),
        LAK(418),
        LBP(422),
        LSL(426),
        ZAR(710),
        LRD(430),
        LYD(434),
        CHF(756),
        MOP(446),
        MKD(807),
        MGA(969),
        MWK(454),
        MYR(458),
        MVR(462),
        MRU(929),
        MUR(480),
        XUA(965),
        MXN(484),
        MXV(979),
        MDL(498),
        MNT(496),
        MAD(504),
        MZN(943),
        MMK(104),
        NAD(516),
        NPR(524),
        NIO(558),
        NGN(566),
        OMR(512),
        PKR(586),
        PAB(590),
        PGK(598),
        PYG(600),
        PEN(604),
        PHP(608),
        PLN(985),
        QAR(634),
        RON(946),
        RUB(643),
        RWF(646),
        SHP(654),
        WST(882),
        STN(930),
        SAR(682),
        RSD(941),
        SCR(690),
        SLL(694),
        SGD(702),
        XSU(994),
        SBD(90),
        SOS(706),
        SSP(728),
        LKR(144),
        SDG(938),
        SRD(968),
        SZL(748),
        SEK(752),
        CHE(947),
        CHW(948),
        SYP(760),
        TWD(901),
        TJS(972),
        TZS(834),
        THB(764),
        TOP(776),
        TTD(780),
        TND(788),
        TRY(949),
        TMT(934),
        UGX(800),
        UAH(980),
        AED(784),
        USN(997),
        UYU(858),
        UYI(940),
        UZS(860),
        VUV(548),
        VEF(937),
        VND(704),
        YER(886),
        ZMW(967),
        ZWL(932),
        XBA(955),
        XBB(956),
        XBC(957),
        XBD(958),
        XTS(963),
        XXX(999),
        XAU(959),
        XPD(964),
        XPT(962),
        XAG(961),
    }


}
