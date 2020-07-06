package com.example.truecallersample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by RavindraP on 06 July 2020
 */
class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        var intent = Intent(context, IncomingCallActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
}