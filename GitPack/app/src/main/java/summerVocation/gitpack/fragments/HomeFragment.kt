package summerVocation.gitpack.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.haibin.calendarview.Calendar
import summerVocation.gitpack.MainActivity
import summerVocation.gitpack.databinding.FragmentHomeBinding


class HomeFragment : Fragment(){
    private var mBinding : FragmentHomeBinding? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val userId = arguments?.getInt("user_Id")
        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        val userId=(activity as MainActivity).getuserId() //메인액티비티에서 유저 아이디 가져오기
        binding.calendarUserIdText.setText(userId.toString())

        binding.tvYear.setText(binding.cvCalendar.getCurYear().toString())
        binding.tvLunar.setText("  "+binding.cvCalendar.getCurMonth().toString())



        binding.cvCalendar.setOnMonthChangeListener { year, month ->
            println("$year , $month")
            initData(year,month)
            binding.tvYear.setText(year.toString())
            binding.tvLunar.setText(" "+month.toString())

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
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    @Override
    protected fun initData(year: Int,month: Int) //캘린더에 특정 색깔 입혀주는 함수
    {

        val mapdata: MutableMap<String,Calendar> = mutableMapOf()
        //1개 커밋 #AAEBAA
        //2~3개 커밋 #46BD7B
        //그이상 #329632
        mapdata.put(
            getSchemeCalendar(year, month, 3, Color.parseColor("#AAEBAA"), "3").toString(),
            getSchemeCalendar(year, month, 3, Color.parseColor("#AAEBAA"), "3")

        )
        mapdata.put(
                getSchemeCalendar(year, month, 6,Color.parseColor("#46BD7B"), "6").toString(),
            getSchemeCalendar(year, month, 6, Color.parseColor("#46BD7B"), "6")
        )
        mapdata.put(
            getSchemeCalendar(year, month, 8, Color.parseColor("#329632"), "3").toString(),
            getSchemeCalendar(year, month, 8, Color.parseColor("#329632"), "3")

        )
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


}