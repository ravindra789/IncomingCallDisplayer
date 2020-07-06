package com.example.truecallersample

import android.content.Context
import android.content.Intent


/**
 * Created by RavindraP on 06 July 2020
 */
class CustomPhoneStateListener : PhoneCallReceiver() {

    override fun onIncomingCallStarted(ctx: Context?, number: String?, start: String?) {
        println("IncomingCall Incoming")
        callDialogActivity(ctx, number)
    }

    override fun onOutgoingCallStarted(ctx: Context?, number: String?, start: String?) {
        println("IncomingCall Outgoing")
        callDialogActivity(ctx, number)
    }

    override fun onIncomingCallEnded(ctx: Context?, number: String?, start: String?, end: String?) {
        //
    }

    override fun onOutgoingCallEnded(ctx: Context?, number: String?, start: String?, end: String?) {
        //
    }

    override fun onMissedCall(ctx: Context?, number: String?, start: String?) {
        //
    }


    private fun callDialogActivity(context: Context?, number: String?) {
        var intent = IncomingCallActivity.createIntent(context, number)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        println("IncomingCall CallDialogActivity - $context - $intent")
        context?.startActivity(intent)
        //context?.startActivity(IncomingCallActivity.createIntent(context, number))
    }

}