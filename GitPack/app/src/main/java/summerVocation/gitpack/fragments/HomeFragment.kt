package summerVocation.gitpack.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.rocketreserver.GetcalendarQuery
import com.example.rocketreserver.GettotalcontributioncountQuery
import com.example.rocketreserver.apolloClient
import com.haibin.calendarview.Calendar
import summerVocation.gitpack.MainActivity
import summerVocation.gitpack.databinding.FragmentHomeBinding
import java.text.DateFormat
import java.text.SimpleDateFormat

class HomeFragment : Fragment(){


    private var mBinding : FragmentHomeBinding? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater,container,false)

        val userId : String=(activity as MainActivity).getuserId() //메인액티비티에서 유저 아이디 가져오기
        binding.calendarUserIdText.setText(userId)
        var curmonth=binding.cvCalendar.getCurMonth()
        var curmonthParse=curmonth.toString()
        if (curmonth<10){
             curmonthParse='0'+curmonth.toString()

        }
        val curYear=binding.cvCalendar.getCurYear().toString()
        binding.tvYear.setText(binding.cvCalendar.getCurYear().toString())
        binding.tvLunar.setText("  "+curmonthParse)

        lifecycleScope.launchWhenResumed { //여기서 totalcontributiondate 받아옴
            val response =apolloClient(requireContext()).query(GettotalcontributioncountQuery(userId)).execute()
            println(userId)
            binding.totalcommitText.setText(response?.data?.user?.contributionsCollection?.contributionCalendar?.totalContributions.toString())
            println(response?.data?.user?.contributionsCollection?.contributionCalendar?.totalContributions)

        }
        lifecycleScope.launchWhenResumed {
            println("--------------------------")
            var pattern: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            var curday = binding.cvCalendar.curDay.toString()
            if (binding.cvCalendar.curDay<10){
                curday="0"+curday.toString()
            }

            var startDate :String = curYear+"-"+curmonthParse+"-01T00:00:00"
            var endDate :String = curYear+"-"+curmonthParse+"-"+curday+"T00:00:00"


            val countlist = mutableListOf<Int>()
            val datelist= mutableListOf<String>()
            val response=apolloClient(requireContext()).query(GetcalendarQuery(userId,startDate,endDate)).execute()
            println(response.data?.user?.contributionsCollection?.contributionCalendar?.weeks)
            var tmp=response.data?.user?.contributionsCollection?.contributionCalendar?.weeks
            var first = tmp?.get(0)

            println(tmp)
//            var firstdata = first?.contributionDays
            if (tmp!!.isNotEmpty()) {
                for (z in tmp!!) { //받은데이터 리스트로 다 변경
                    var firstdata = z.contributionDays
                    for (i in firstdata!!) {
                        datelist.add(i.date.toString())
                        countlist.add(i.contributionCount)
                    }
                }
            }
            println("_________________")
            println(countlist.count())
            if(countlist[countlist.count()-1]==0){
                binding.todaycommit.setText("오늘 아직 커밋을 하지 않았습니다 ㅠ")
            }else{
                binding.todaycommit.setText("오늘 커밋을 했네요!")
            }
            println(datelist)
            println(countlist)
            initData(binding.cvCalendar.getCurYear(),binding.cvCalendar.curMonth,countlist,datelist)

        }



        binding.cvCalendar.setOnMonthChangeListener { year, month ->
            println("$year , $month")
//            initData(year,month)
            binding.tvYear.setText(year.toString())
            binding.tvLunar.setText(" "+month.toString())
            if ( (year==curYear.toInt() && month<=curmonth) || year<curYear.toInt()){//현재날짜가 지금 보여지고있는 날짜보다 미래일때 즉 과거를 열람할때만 발동
                var monthParse=month.toString()
                if (month<10){
                     monthParse='0'+month.toString()
                }
                var startDate :String = year.toString()+"-"+monthParse+"-01T00:00:00"
                var endDate :String = year.toString()+"-"+monthParse+"-"+31+"T00:00:00"
                val countlist = mutableListOf<Int>()
                val datelist= mutableListOf<String>()
                lifecycleScope.launchWhenResumed {
                    val response=apolloClient(requireContext()).query(GetcalendarQuery(userId,startDate,endDate)).execute()
                    println(response.data?.user?.contributionsCollection?.contributionCalendar?.weeks)
                    var tmp=response.data?.user?.contributionsCollection?.contributionCalendar?.weeks
                    println("______________________________")
                    println(tmp)
                    if (tmp!=null) {
                        for (z in tmp!!) { //받은데이터 리스트로 다 변경
                            var firstdata = z.contributionDays
                            for (i in firstdata!!) {
                                datelist.add(i.date.toString())
                                countlist.add(i.contributionCount)
                            }
                        }
                        println(countlist)
                        initData(year,month,countlist,datelist)
                    }
                }


            }

        }



        binding.lastMonth.setOnClickListener {
            binding.cvCalendar.scrollToNext()
        }
        binding.preMonth.setOnClickListener {
            binding.cvCalendar.scrollToPre()
        }

        mBinding =binding
        return mBinding?.root

    }

    override fun onDestroyView() { // 프래그먼트 삭제될때 자동으로실행
        mBinding=null
        super.onDestroyView()
        println("홈프래그먼트 삭제")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    @Override
    protected fun initData(year: Int,month: Int,list: MutableList<Int>,datelist: MutableList<String>) //캘린더에 특정 색깔 입혀주는 함수
    {
        val mapdata: MutableMap<String,Calendar> = mutableMapOf()
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

        mBinding!!.cvCalendar.setSchemeDate(mapdata)



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

    //gradlew :app:downloadApolloSchema --endpoint=https://api.github.com/graphql --header="Authorization: bearer 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" --schema=app/src/main/graphql/com.exmaple.rocketreserver/schema.json
//gradlew :app:downloadApolloSchema --endpoint=https://api.github.com/graphql --header="Authorization: bearer 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" --schema=app/src/main/graphql/com.exmaple.rocketreserver/schema.graphql
}