package summerVocation.gitpack.service

import android.app.Service
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.IBinder
import com.example.rocketreserver.GetcalendarQuery
import com.example.rocketreserver.apolloClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

            if (userId != null) {
                val now =System.currentTimeMillis()//현재시간받아오기
                val date = Date(now)
                var pattern: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val getnowTime : String=pattern.format(date)//원하는 패턴으로 date객체 문자열화하는 함수
                println(getnowTime)
                if(date.hours==16 && date.minutes==41){
                    var parseDate: String=getnowTime.substring(8 until 10)
                    var parseMonth: String=getnowTime.substring(5 until 7)
                    var parseYear: String=getnowTime.substring(0 until 4)

                    var startDate :String = parseYear+"-"+parseMonth+"-"+parseDate+"T00:00:00"
                    var endDate :String = parseYear+"-"+parseMonth+"-"+parseDate+"T10:00:00"
                    println(endDate)
                    val countlist = mutableListOf<Int>()
                    val datelist= mutableListOf<String>()
                    CoroutineScope(Dispatchers.IO).launch {
                        val response= apolloClient(this@checkTodayCommitService).query(GetcalendarQuery(userId,startDate,endDate)).execute()
                        val tmp=response.data?.user?.contributionsCollection?.contributionCalendar?.weeks
                        val todayCommit= tmp!![0].contributionDays[0].contributionCount//이 변수에 현재 커밋수 들어있음
                        println(todayCommit)

                    }

                    }
            }

        }


        return super.onStartCommand(intent, flags, startId)
    }
}