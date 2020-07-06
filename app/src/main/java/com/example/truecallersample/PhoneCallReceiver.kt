package com.example.truecallersample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by RavindraP on 06 July 2020
 */
abstract class PhoneCallReceiver: BroadcastReceiver() {

    private var isIncoming = false
    private var savedNumber: String? = null
    private val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
    private var callStartTime: Date? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        println("IncomingCall onReceive")
        if (intent!!.action == "android.intent.action.NEW_OUTGOING_CALL") {
            savedNumber =intent.extras!!.getString("android.intent.extra.PHONE_NUMBER")
        }else{
            var number: String
            try {
                val stateStr = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
                number = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)!!
                if (number == null) {
                    number = intent.extras!!.getString("android.intent.extra.PHONE_NUMBER")!!
                }
                println(savedNumber + " saved Number ####### " + number + " Else")
                var state = 0
                when (stateStr) {
                    TelephonyManager.EXTRA_STATE_IDLE -> {
                        state = TelephonyManager.CALL_STATE_IDLE
                    }
                    TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                        state = TelephonyManager.CALL_STATE_OFFHOOK
                    }
                    TelephonyManager.EXTRA_STATE_RINGING -> {
                        state = TelephonyManager.CALL_STATE_RINGING
                    }
                }
                if (number != null) {
                    savedNumber = number
                }
                println("IncomingCall Number - $savedNumber")
                onCallStateChanged(
                    context,
                    state,
                    savedNumber
                )
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }

    }


    //Derived classes should override these to respond to specific events of interest
    abstract fun onIncomingCallStarted(
        ctx: Context?,
        number: String?,
        start: String?
    )

    abstract fun onOutgoingCallStarted(
        ctx: Context?,
        number: String?,
        start: String?
    )

    abstract fun onIncomingCallEnded(
        ctx: Context?,
        number: String?,
        start: String?,
        end: String?
    )

    abstract fun onOutgoingCallEnded(
        ctx: Context?,
        number: String?,
        start: String?,
        end: String?
    )

    abstract fun onMissedCall(
        ctx: Context?,
        number: String?,
        start: String?
    )

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    private fun onCallStateChanged(
        context: Context?,
        state: Int,
        number: String?
    ) {
        if (context != null) {
            if (Config.lastState === state) {
                println("IncomingCall No change return")
                //No change, debounce extras
                return
            }
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    println("IncomingCall Broad - Incoming")
                    isIncoming = true
                    callStartTime = Date()
                    savedNumber = number
                    onIncomingCallStarted(
                        context,
                        number,
                        formatter.format(callStartTime)
                    )
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    if (Config.lastState !== TelephonyManager.CALL_STATE_RINGING) {
                        println("IncomingCall Broad - Outgoing")
                        isIncoming = false
                        callStartTime = Date()
                        onOutgoingCallStarted(context, savedNumber, formatter.format(callStartTime))
                    }
                }
                TelephonyManager.CALL_STATE_IDLE -> {
                    //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                    if (Config.lastState == TelephonyManager.CALL_STATE_RINGING) {
                        println("IncomingCall Broad - Missed Call")
                        //Ring but no pickup-  a miss
                        onMissedCall(
                            context,
                            savedNumber,
                            formatter.format(callStartTime)
                        )
                    } else if (isIncoming) {
                        if (context != null && savedNumber != null && formatter.format(Date()) != null && callStartTime != null) {
                            println("IncomingCall Broad - IncomingEnd")
                            onIncomingCallEnded(
                                context,
                                savedNumber,
                                formatter.format(
                                    callStartTime
                                ),
                                formatter.format(
                                    Date()
                                )
                            )
                        }
                    } else {
                        if (context != null && savedNumber != null && formatter.format(
                                Date()
                            ) != null && callStartTime != null
                        ) {
                            println("IncomingCall Broad - OutgoingEnd")
                            onOutgoingCallEnded(
                                context,
                                savedNumber,
                               formatter.format(
                                    callStartTime
                                ),
                                formatter.format(
                                    Date()
                                )
                            )
                        }
                    }
                }
            }
            Config.lastState = state
        }
    }

}