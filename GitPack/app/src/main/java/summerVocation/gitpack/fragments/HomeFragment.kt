package summerVocation.gitpack.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.rocketreserver.GettotalcontributioncountQuery
import com.example.rocketreserver.apolloClient
import summerVocation.gitpack.MainActivity
import summerVocation.gitpack.databinding.FragmentHomeBinding
import summerVocation.gitpack.viewmodel.calaenderViewModel

class HomeFragment : Fragment(){


    private val mycalaenderViewModel by activityViewModels<calaenderViewModel>()
    private var mBinding : FragmentHomeBinding? =null
    lateinit var userId:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        userId=(activity as MainActivity).getuserId() //메인액티비티에서 유저 아이디 가져오기
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
            val response =
                apolloClient(requireContext()).query(GettotalcontributioncountQuery(userId)).execute()
            println(userId)
            binding.totalcommitText.setText(response?.data?.user?.contributionsCollection?.contributionCalendar?.totalContributions.toString())
            println(response?.data?.user?.contributionsCollection?.contributionCalendar?.totalContributions)

        }

        mycalaenderViewModel.checknowday.observe(viewLifecycleOwner, Observer {
            mBinding!!.todaycommit.text=it
        })
        mycalaenderViewModel.currentScheme.observe(viewLifecycleOwner, Observer {
            mBinding!!.cvCalendar.setSchemeDate(it)
            println(it)

        })
//        mycalaenderViewModel.scheme.observe(viewLifecycleOwner, Observer {
//            mBinding!!.cvCalendar.setSchemeDate(it)
//        })
        mycalaenderViewModel.updateMonth(binding.cvCalendar.curYear, binding.cvCalendar.curMonth,binding.cvCalendar.curDay,1)
        binding.cvCalendar.setOnMonthChangeListener { year, month ->
            binding.tvYear.setText(year.toString())
           binding.tvLunar.setText(" "+month.toString())
            mycalaenderViewModel.updateMonth(year, month)

        }
        binding.lastMonth.setOnClickListener {
            binding.cvCalendar.scrollToNext()
        }
        binding.preMonth.setOnClickListener {
            binding.cvCalendar.scrollToPre()
        }

        mBinding =binding
        initView()
        return mBinding?.root

    }
    fun initView() {


    }

    override fun onDestroyView() { // 프래그먼트 삭제될때 자동으로실행
        mBinding=null
        super.onDestroyView()
        println("홈프래그먼트 삭제")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    //gradlew :app:downloadApolloSchema --endpoint=https://api.github.com/graphql --header="Authorization: bearer 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" --schema=app/src/main/graphql/com.exmaple.rocketreserver/schema.json
//gradlew :app:downloadApolloSchema --endpoint=https://api.github.com/graphql --header="Authorization: bearer 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" --schema=app/src/main/graphql/com.exmaple.rocketreserver/schema.graphql
}