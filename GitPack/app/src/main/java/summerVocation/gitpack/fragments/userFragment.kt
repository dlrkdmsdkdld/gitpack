package summerVocation.gitpack.fragments

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rocketreserver.GetUserImageURLQuery
import com.example.rocketreserver.GetlanguagesQuery
import com.example.rocketreserver.apolloClient
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_user.*
import summerVocation.gitpack.MainActivity
import summerVocation.gitpack.R
import summerVocation.gitpack.SQLiteDBHelper
import summerVocation.gitpack.databinding.FragmentUserBinding


class userFragment : Fragment() {
    private var mBinding : FragmentUserBinding? =null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater,container,false)
        val userId : String=(activity as MainActivity).getuserId() //메인액티비티에서 유저 아이디 가져오기
        binding.userIdTextview.text = userId

        lifecycleScope.launchWhenResumed {
            val response =
                apolloClient(requireContext()).query(GetUserImageURLQuery(userId)).execute()
            val userURL = response?.data?.user?.avatarUrl
            Glide.with(requireActivity())
                .load(userURL)
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(binding.userImage)
            println("$$$$$$$$$$$$$$$$$$$$$$$")
        }

        binding.btnlogout.setOnClickListener {
            // sqlite 테이블 없애는 함수
            val sqlDrop : String = "DROP TABLE if exists tb_login"
            val db: SQLiteDatabase = SQLiteDBHelper(requireContext() as MainActivity).writableDatabase
            db.execSQL(sqlDrop)
            onDestroy()
            ActivityCompat.finishAffinity(requireActivity());


        }
        lifecycleScope.launchWhenResumed { //여기서 totalcontributiondate 받아옴
            val response =
                apolloClient(requireContext()).query(GetlanguagesQuery(userId)).execute()
            val tmp = response?.data?.user?.repositories?.nodes
            var languageData : MutableMap<String,Int> = mutableMapOf()
            var k = mutableListOf<String>()
            val step = tmp!!.count()
            println(step)
            for(i in 0..step-1){
                println(tmp!![i]!!.primaryLanguage?.name)
                k.add(tmp!![i]!!.primaryLanguage?.name.toString())
            }
            println(k)
            var setL = k.toSet()
            println(setL)

            for( i in setL){
                languageData.put(i,0)
            }
            for(i in k){
                languageData[i] = languageData[i]!!+1
            }
            val pieData=parseMapToPie(languageData)
            makePieChart(pieData)


        }

        mBinding =binding
        return mBinding?.root
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun parseMapToPie(languageData : MutableMap<String,Int>):PieData{
        val yValues: ArrayList<PieEntry> = ArrayList()
        languageData.forEach { languagename, count ->
            yValues.add(PieEntry(count.toFloat(),languagename))
        }
        Log.d("now","yYale : $yValues")
        val dataSet: PieDataSet = PieDataSet(yValues, "")
        with(dataSet) {
            sliceSpace = 3f
            selectionShift = 5f
            setColors(*ColorTemplate.LIBERTY_COLORS)
        }
        val pieData: PieData = PieData(dataSet)
        with(pieData) {
            setValueTextSize(10f)
            setValueTextColor(Color.BLACK)

        }
        return pieData
    }
    fun makePieChart(pieData: PieData){
        user_piechart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)//부모 레이아웃을 고려해서 margin 설정
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            centerText="language"
            setCenterTextSize(20f)
            setCenterTextColor(Color.LTGRAY)
//            transparentCircleRadius = 61f // 각 파이당 안에 흰 공백 설정
            data = pieData//데이터를 파이에 넣음
            setEntryLabelColor(Color.BLACK)//각 파이들마다 이름 색깔
            invalidate()
        }


    }

    override fun onDestroyView() { // 프래그먼트 삭제될때 자동으로실행
        mBinding=null
        super.onDestroyView()

    }


    override fun onDestroy() {
        super.onDestroy()
    }
}