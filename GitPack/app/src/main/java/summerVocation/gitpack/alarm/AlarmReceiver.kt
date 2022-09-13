package summerVocation.gitpack.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.rocketreserver.GetcalendarQuery
import com.example.rocketreserver.apolloClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import summerVocation.gitpack.R
import summerVocation.gitpack.SQLiteDBHelper
import summerVocation.gitpack.utils.Constant.TAG
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val PRIMARY_CHANNEL_ID = "AlarmTest_Notification_channel"
    }

    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "알람리시버 실행 onReceive: ")
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
       // val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp:AlarmTest")
       // wl.acquire(10 * 60 * 1000L /*10 minutes*/)
        val alarmSettingManager = Alarmmanager(context)
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            alarmSettingManager.setAlarmManager()
        } else {
            //alarmSettingManager.setAlarmManager()
            Log.d(TAG, "알람리시버 실행중 ")
            var userId: String? = null
            val db: SQLiteDatabase = SQLiteDBHelper(context).readableDatabase
            try{
                val cursor = db.rawQuery("select * from tb_login", null)
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getString(0)
                        //binding.mainText.setText(userId)
                    }
                }
                Log.d(TAG, "userId: $userId ")
                db.close()
            }catch(e: Exception){
                println("---")
            }
            if (userId != null && isNetworkAvailable(context)) {//유저아이디가 저장되어있고 인터넷연결됐을때 if문 실행
                val now =System.currentTimeMillis()//현재시간받아오기
                Log.d(TAG, "커밋했는지 체크중 $now ")
                val date = Date(now)
                var pattern: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val getnowTime : String=pattern.format(date)//원하는 패턴으로 date객체 문자열화하는 함수
                println(date)
                var parseDate: String=getnowTime.substring(8 until 10)
                var parseMonth: String=getnowTime.substring(5 until 7)
                var parseYear: String=getnowTime.substring(0 until 4)

                var startDate :String = parseYear+"-"+parseMonth+"-"+parseDate+"T00:00:00"
                var endDate :String = parseYear+"-"+parseMonth+"-"+parseDate+"T22:00:00"
                CoroutineScope(Dispatchers.IO).launch {
                    val response= apolloClient(context).query(
                            GetcalendarQuery(userId,startDate,endDate)
                        ).execute()
                    val tmp=response.data?.user?.contributionsCollection?.contributionCalendar?.weeks
                    Log.d(TAG, "커밋했는지 체크중2 $tmp ")
                    val todayCommit= tmp!![0].contributionDays[0].contributionCount//이 변수에 현재 커밋수 들어있음
                    println(todayCommit)
                    Log.d(TAG, "todayCommit:$todayCommit ")
                    if(todayCommit==0){
                            makeNotification(context,1)
                        }
                    else{
                        makeNotification(context,2)
                    }

                    }


            }



        }
    }

    fun makeNotification(context:Context,type:Int){
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder:NotificationCompat.Builder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelId = "one-channel"
            val channelName = "CheckTodayCommit"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "My Channel CheckTodayCommit"
            channel.setShowBadge(true)

            channel.enableVibration(true)
            channel.vibrationPattern  = longArrayOf(100,200,100,200)
            notificationManager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(context,channelId)
        }else{
            builder = NotificationCompat.Builder(context)
        }
        builder.setSmallIcon(R.drawable.ic_baseline_api_24)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("GitPack")
        if (type==1){
            builder.setContentText("오늘 아직 커밋을 하지 않았습니다.")
        }else{
            builder.setContentText("오늘 커밋했네요!")

        }
        notificationManager.notify(1,builder.build())
    }
    private fun isNetworkAvailable(context: Context): Boolean {//인터넷 연결되어있는지 체크하는 코드
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }
}