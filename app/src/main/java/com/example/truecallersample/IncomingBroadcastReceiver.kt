package com.example.truecallersample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager


/**
 * Created by RavindraP on 06 July 2020
 */
class IncomingBroadcastReceiver: BroadcastReceiver() {

    private var state: Int = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        val telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val bundle = intent!!.extras
        val phoneNumber: String? = bundle?.getString("incoming_number")
        val stateStr = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)

        when {
            stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE) -> {
                state = TelephonyManager.CALL_STATE_IDLE
            }
            stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) -> {
                state = TelephonyManager.CALL_STATE_OFFHOOK
            }
            stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING) -> {
                state = TelephonyManager.CALL_STATE_RINGING
            }
        }
        if (phoneNumber == null || "" == phoneNumber) {
            return
        }

        var phoneStateListener = CustomPhoneStateListener.newInstance(context, state, phoneNumber)
        telephonyManager.listen(
            phoneStateListener,
            PhoneStateListener.LISTEN_CALL_STATE
        )
        phoneStateListener.onCallStateChanged(state, phoneNumber)
    }
}