package summerVocation.gitpack.fragments

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rocketreserver.GetUserImageURLQuery
import com.example.rocketreserver.GetlanguagesQuery
import com.example.rocketreserver.apolloClient
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


//        chart.setPieChart(pieChart)
//        chart.showLegend(legendLayout)

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
            val db: SQLiteDatabase = SQLiteDBHelper(requireContext()).writableDatabase
            db.execSQL(sqlDrop)
            onDestroy()
            ActivityCompat.finishAffinity(requireActivity());
//            (activity as MainActivity).onDe


        }
        lifecycleScope.launchWhenResumed { //여기서 totalcontributiondate 받아옴
            val response =
                apolloClient(requireContext()).query(GetlanguagesQuery(userId)).execute()
            println("____-----------")
            val tmp = response?.data?.user?.repositories?.nodes
            var languageData : MutableMap<String,Int> = mutableMapOf()
            var k = mutableListOf<String>()
            val step = tmp!!.count()
            println(step)
            for(i in 0..step-1){
                println(tmp!![i]!!.primaryLanguage?.name)
                k.add(tmp!![i]!!.primaryLanguage?.name.toString())
            }
//            println(response?.data?.user?.repositories)
            println(k)
            var setL = k.toSet()
            println(setL)

            for( i in setL){
                languageData.put(i,0)
            }
            for(i in k){
                languageData[i] = languageData[i]!!+1
            }
            println(languageData)
            val pieChart = PieChart(
                slices = provideSlices(languageData), clickListener = null, sliceStartPoint = 0f, sliceWidth = 80f
            ).build()
            binding.chart.setPieChart(pieChart)
            binding.chart.showLegend(binding.legendLayout)
        }



        mBinding =binding
        return mBinding?.root
    }

    override fun onDestroyView() { // 프래그먼트 삭제될때 자동으로실행
        mBinding=null
        super.onDestroyView()

    }
    private fun provideSlices(tmp:MutableMap<String,Int>): ArrayList<Slice> {
        var result= ArrayList<Slice>()
        var colorset = ArrayList<Int>()
        colorset.add(R.color.olive)
        colorset.add(R.color.red)
        colorset.add(R.color.gray)
        colorset.add(R.color.margenta)
        colorset.add(R.color.yellow)
        colorset.add(R.color.teal_700)
        colorset.add(R.color.teal_200)
        colorset.add(R.color.blue)
        colorset.add(R.color.darkGold)
        colorset.add(R.color.darkblue)
        colorset.add(R.color.orange)

        var z =0
        for (i in tmp){
            if(z<colorset.count()){
                var a =Slice(i.value.toFloat(),colorset[z],i.key)
                result.add(a)
                z+=1
            }
            else{
                z=0
                var a =Slice(i.value.toFloat(),colorset[z],i.key)
                result.add(a)
            }


        }
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}