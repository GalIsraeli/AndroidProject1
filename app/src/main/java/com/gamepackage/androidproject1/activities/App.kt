package com.gamepackage.androidproject1.activities
import android.app.Application
import com.gamepackage.androidproject1.utils.MSPV3
import com.gamepackage.androidproject1.utils.MySignal

class App :Application() {
    override fun onCreate() {
        super.onCreate()
        MSPV3.init(this)
        MySignal.init(this)
    }
}