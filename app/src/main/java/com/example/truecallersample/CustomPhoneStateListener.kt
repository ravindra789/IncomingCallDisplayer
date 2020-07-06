package com.example.truecallersample

import android.R.attr
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import java.util.*


/**
 * Created by RavindraP on 06 July 2020
 */
class CustomPhoneStateListener : PhoneStateListener() {

    private var mContext: Context? = null
    private var mLastState = TelephonyManager.CALL_STATE_IDLE
    private var mPhoneNumber: String? = null
    private var isIncoming = false


    companion object{
        private var callStartTime: Date? = null
        fun newInstance(
            context: Context,
            state: Int,
            phoneNumber: String
        ):CustomPhoneStateListener = CustomPhoneStateListener().apply {
            mContext = context
            mLastState = state
            mPhoneNumber = phoneNumber
        }
    }

    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        super.onCallStateChanged(state, phoneNumber)

        if(mLastState == state){
            return
        }

        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {
                if (mLastState === TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    Toast.makeText(
                        mContext,
                        "Ringing but no pickup" + mPhoneNumber + " Call time " + callStartTime + " Date " + Date(),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (isIncoming) {
                    isIncoming = true
                    callStartTime = Date()
                    Thread.sleep(2000)
                    callDialogActivity()
                }else{
                    Toast.makeText(
                        mContext,
                        "outgoing " + attr.phoneNumber + " Call time " + callStartTime + " Date " + Date(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                if (mLastState !== TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
                    Toast.makeText(
                        mContext,
                        "Outgoing Call Started $mPhoneNumber",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            TelephonyManager.CALL_STATE_RINGING -> try {
                isIncoming = true
                callStartTime = Date()
            } catch (e: Exception) {
                e.localizedMessage
            }
            else -> {}
        }
        mLastState = state
    }

    private fun callDialogActivity() {
        var intent = Intent(mContext, IncomingCallActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

}