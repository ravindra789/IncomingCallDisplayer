package com.example.truecallersample

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by RavindraP on 06 July 2020
 */
class IncomingCallActivity : AppCompatActivity() {

    private var dialog: AlertDialog.Builder? = null
    private var mPhoneNumber: String? = null
    var alert: AlertDialog? = null

    companion object {
        fun createIntent(context: Context?, phoneNumber: String?): Intent {
            return Intent(context, IncomingCallActivity::class.java).apply {
                println("IncomingCall IncomingActivity 0 ")
                putExtra("mobile", phoneNumber)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        println("IncomingCall IncomingActivity 1 ")
        dialog = AlertDialog.Builder(this)
        val extras = intent.extras
        if (extras != null) {
            mPhoneNumber = extras.getString("mobile")
        }
        showMessage()
    }

    private fun showMessage() {
        dialog?.setTitle("Call Details")?.setMessage("Mobile Number - $mPhoneNumber")
        alert = dialog?.create()
        alert?.show()
    }


    /* override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_incoming_call)
         println("IncomingCall Activity")

         try {
             window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
             window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)

             var number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
             txtPhoneNumber.text = "Incoming call from $number"
         }
         catch (e:Exception) {}
     }*/

}