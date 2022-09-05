package summerVocation.gitpack.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rocketreserver.GetcalendarQuery
import com.example.rocketreserver.apolloClient
import com.haibin.calendarview.Calendar
import kotlinx.coroutines.launch

class calaenderViewModel( application: Application) : AndroidViewModel(application){
    companion object{
        const val TAG= "카카오 auth"
    }
    private val context = getApplication<Application>().applicationContext
    private var userId = MutableLiveData<String>()
    val currentUserId: LiveData<String>
        get() = userId
    fun updateId(input:String){
        userId.value=input
    }
    var checknowday = MutableLiveData<String>()
    var scheme = MutableLiveData<MutableMap<String,Calendar>>()
//    init {
//        val now =System.currentTimeMillis()//현재시간받아오기
//        val date = Date(now)
//        var pattern: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//        val getnowTime : String=pattern.format(date)//원하는 패턴으로 date객체 문자열화하는 함수
//        val Year = getnowTime.substring(0 until 4).toInt()
//        val month = getnowTime.substring(5 until 7).toInt()
//        val day = getnowTime.substring(8 until 10).toInt()
//        Log.i(TAG,Year.toString()+" " +month.toString()+" "+day.toString())
//        updateMonth(Year,month,day)
//
//    }
    val currentScheme: MutableLiveData<MutableMap<String, Calendar>>
        get() = scheme
    fun updateMonth(Year:Int, Month:Int,Day:Int=31,check:Int=0){
        val userIdvalue = userId.value
        var curmonthParse:String=Month.toString()
        var curdayParse:String=Day.toString()
        if (Month<10){
            curmonthParse='0'+Month.toString()
        }
        if (Day<10){
            curdayParse="0"+Day.toString()
        }
        val startDate :String = Year.toString()+"-"+curmonthParse+"-01T00:00:00"
        val endDate :String = Year.toString()+"-"+curmonthParse+"-"+curdayParse+"T00:00:00"
        var countitems: ArrayList<Int> = ArrayList()
        var dateitems: ArrayList<String> = ArrayList()
        viewModelScope.launch {
            val response=
                apolloClient(context).query(GetcalendarQuery(userIdvalue!!,startDate,endDate)).execute()
            var tmp=response.data?.user?.contributionsCollection?.contributionCalendar?.weeks
            println(tmp)
            if (tmp!!.isNotEmpty()) {
                for (z in tmp!!) { //받은데이터 리스트로 다 변경
                    var firstdata = z.contributionDays
                    for (i in firstdata!!) {
                        dateitems.add(i.date.toString())
                        countitems.add(i.contributionCount)
                    }
                }
            }
            if (check==1){
                if(countitems[countitems.count()-1]==0){
                    checknowday.value= "오늘 아직 커밋을 하지 않았습니다 ㅠ"
                }else{
                    checknowday.value="오늘 커밋을 했네요!"
                }

            }
            val result = initData(Year,Month,countitems,dateitems)
            scheme.value=result



        }
    }
    @Override
    protected fun initData(year: Int,month: Int,list: MutableList<Int>,datelist: MutableList<String>): MutableMap<String, Calendar> //캘린더에 특정 색깔 입혀주는 함수
    {
        val mapdata: MutableMap<String, Calendar> = mutableMapOf()
        //1개 커밋 #AAEBAA
        //2~3개 커밋 #46BD7B
        //그이상 #329632
        val listcount = list.count()
        var nowFocusMonth :String=year.toString()+"-"+month
        if(month<10){
            nowFocusMonth=year.toString()+"-0"+month
        }
        println(nowFocusMonth)

        for ( i in 0..listcount-1){
            println(datelist[i].substring(0 until 7))
            if(datelist[i].substring(0 until 7)==nowFocusMonth) {//달별로 마지막날이 31일이 아닌경우도 있기때문에 위와같이 함
                when (list[i]) {
                    0 -> println("0개0")
                    1 -> mapdata.put(
                        getSchemeCalendar(
                            year,
                            month,
                            i + 1,
                            Color.parseColor("#AAEBAA"),
                            ""
                        ).toString(),
                        getSchemeCalendar(year, month, i + 1, Color.parseColor("#AAEBAA"), "")
                    )
                    in 2..3 -> mapdata.put(
                        getSchemeCalendar(
                            year,
                            month,
                            i + 1,
                            Color.parseColor("#46BD7B"),
                            ""
                        ).toString(),
                        getSchemeCalendar(year, month, i + 1, Color.parseColor("#46BD7B"), "")
                    )
                    else -> mapdata.put(
                        getSchemeCalendar(
                            year,
                            month,
                            i + 1,
                            Color.parseColor("#329632"),
                            ""
                        ).toString(),
                        getSchemeCalendar(year, month, i + 1, Color.parseColor("#329632"), "")
                    )
                }
            }
        }
        println(mapdata)
        return mapdata


    }
    private fun getSchemeCalendar(year: Int, month: Int, day: Int, color:Int, text: String
    ): Calendar { //캘린더 리턴해주는 함수
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text

        return calendar
    }

    override fun onCleared() {
        super.onCleared()
        println("뷰모델 삭제됨")
    }
}