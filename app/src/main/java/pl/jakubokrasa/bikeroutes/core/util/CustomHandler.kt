package pl.jakubokrasa.bikeroutes.core.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

class CustomHandler(looper: Looper) : Handler(looper) {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)

        Log.d("CustomHandler", "Inside handle message " + msg?.what)
    }
}