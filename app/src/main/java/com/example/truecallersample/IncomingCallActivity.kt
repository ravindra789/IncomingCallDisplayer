package com.example.truecallersample

import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_incoming_call.*

/**
 * Created by RavindraP on 06 July 2020
 */
class IncomingCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_call)

        try {
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)

            var number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            txtPhoneNumber.text = "Incoming call from $number"
        }
        catch (e:Exception) {}

    }

}