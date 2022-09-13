package summerVocation.gitpack.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import summerVocation.gitpack.utils.Constant.TAG
import java.util.*

class Alarmmanager(private val context: Context) {
//    private val alarmManager: AlarmManager by lazy {
//        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    }
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarmManager() {
        Log.d(TAG, "setAlarmManager:실행  ")
        val pendingIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE,30)
            set(Calendar.SECOND,0)
            //set(Calendar.SECOND,59)
           // set(Calendar.MINUTE, 18)
        }

        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )


       // val ac = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent);


        //alarmManager.setAlarmClock(ac,pendingIntent)
    }
}