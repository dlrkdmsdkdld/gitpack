package summerVocation.gitpack.fragments

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.faskn.lib.PieChart
import com.faskn.lib.Slice
import summerVocation.gitpack.MainActivity
import summerVocation.gitpack.R
import summerVocation.gitpack.SQLiteDBHelper
import summerVocation.gitpack.databinding.FragmentUserBinding


class userFragment : Fragment() {
    private var mBinding : FragmentUserBinding? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater,container,false)
        val userId : String=(activity as MainActivity).getuserId() //메인액티비티에서 유저 아이디 가져오기
        binding.userIdTextview.text = userId

        val pieChart = PieChart(
            slices = provideSlices(), clickListener = null, sliceStartPoint = 0f, sliceWidth = 80f
        ).build()
        binding.chart.setPieChart(pieChart)
        binding.chart.showLegend(binding.legendLayout)
//        chart.setPieChart(pieChart)
//        chart.showLegend(legendLayout)



        binding.btnlogout.setOnClickListener {
            // sqlite 테이블 없애는 함수
            val sqlDrop : String = "DROP TABLE if exists tb_login"
            val db: SQLiteDatabase = SQLiteDBHelper(requireContext()).writableDatabase
            db.execSQL(sqlDrop)

        }


        mBinding =binding
        return mBinding?.root
    }

    override fun onDestroyView() { // 프래그먼트 삭제될때 자동으로실행
        mBinding=null
        super.onDestroyView()
    }
    private fun provideSlices(): ArrayList<Slice> {
        return arrayListOf(
            Slice(
                2000.toFloat(),
                R.color.purple_200,
                "Google"
            ),
            Slice(
                1000.toFloat(),
                R.color.black,
                "Facebook"
            ),
            Slice(
                4000.toFloat(),
                R.color.teal_200,
                "Twitter"
            ),
            Slice(
                10000.toFloat(),
                R.color.olive,
                "Other"
            )
        )
    }
}