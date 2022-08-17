package summerVocation.gitpack.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.rocketreserver.GetcalendarQuery
import com.example.rocketreserver.apolloClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import summerVocation.gitpack.R
import summerVocation.gitpack.SQLiteDBHelper
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class checkTodayCommitService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent == null){
            return Service.START_STICKY
        }else {
            println("서비스실행")
            var userId: String? = null
            val db: SQLiteDatabase = SQLiteDBHelper(this).readableDatabase
            try{
                val cursor = db.rawQuery("select * from tb_login", null)
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getString(0)
                        //binding.mainText.setText(userId)
                    }
                }
                db.close()
            }catch(e: Exception){
                println("---")
            }

            if (userId != null && isNetworkAvailable(this)) {//유저아이디가 저장되어있고 인터넷연결됐을때 if문 실행
                val now =System.currentTimeMillis()//현재시간받아오기
                val date = Date(now)
                var pattern: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val getnowTime : String=pattern.format(date)//원하는 패턴으로 date객체 문자열화하는 함수
                println(date)
                if(date.hours==22 && date.minutes==0){
                    var parseDate: String=getnowTime.substring(8 until 10)
                    var parseMonth: String=getnowTime.substring(5 until 7)
                    var parseYear: String=getnowTime.substring(0 until 4)

                    var startDate :String = parseYear+"-"+parseMonth+"-"+parseDate+"T00:00:00"
                    var endDate :String = parseYear+"-"+parseMonth+"-"+parseDate+"T22:00:00"
                    println("------")
                    println(endDate)
                    val countlist = mutableListOf<Int>()
                    val datelist= mutableListOf<String>()
                    CoroutineScope(Dispatchers.IO).launch {
                        val response= apolloClient(this@checkTodayCommitService).query(GetcalendarQuery(userId,startDate,endDate)).execute()
                        val tmp=response.data?.user?.contributionsCollection?.contributionCalendar?.weeks
                        println(tmp)
                        val todayCommit= tmp!![0].contributionDays[0].contributionCount//이 변수에 현재 커밋수 들어있음
                        println(todayCommit)
                        if(todayCommit==0){
                            makeNotification()
                        }

                    }

                    }
            }

        }


        return super.onStartCommand(intent, flags, startId)
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
    fun makeNotification(){
        val manager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
            manager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this,channelId)
        }else{
            builder = NotificationCompat.Builder(this)
        }
        builder.setSmallIcon(R.drawable.ic_baseline_api_24)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("GitPack")
        builder.setContentText("오늘 아직 커밋을 하지 않았습니다.")
        manager.notify(1,builder.build())
    }
}