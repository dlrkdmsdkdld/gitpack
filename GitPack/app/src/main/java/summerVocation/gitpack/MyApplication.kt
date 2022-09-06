package summerVocation.gitpack

import android.app.Application

class MyApplication :Application() {

    companion object{
        lateinit var prefs: PreferenceUtil
        lateinit var instance:MyApplication
            private set
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        instance=this
        super.onCreate()
    }


}