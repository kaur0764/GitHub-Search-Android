package jasreet.mad9132.jasreet_final_project

/*
* Created by Jasreet Kaur on November 24, 2022
*/

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class TheApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

}