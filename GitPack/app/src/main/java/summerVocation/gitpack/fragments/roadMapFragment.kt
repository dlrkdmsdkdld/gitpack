package summerVocation.gitpack.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_roadmap.*
import summerVocation.gitpack.DetailRoadmap
import summerVocation.gitpack.R
import summerVocation.gitpack.RecylerView.RecyclerAdapter
import summerVocation.gitpack.RecylerView.RecyclerModel
import summerVocation.gitpack.RecylerView.RecyclerViewInterface
import summerVocation.gitpack.databinding.FragmentRoadmapBinding

class roadMapFragment : Fragment(),RecyclerViewInterface {
    private lateinit var myRecyclerAdapter: RecyclerAdapter
    private var mBinding : FragmentRoadmapBinding? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRoadmapBinding.inflate(inflater,container,false)


        mBinding =binding


        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var modelList = ArrayList<RecyclerModel>()
        val myModel = RecyclerModel(name = "frontend", profileImage = R.drawable.icons8frontend2 )
        modelList.add(myModel)
        val myModel2 = RecyclerModel(name = "Backend", profileImage =R.drawable.icons8backenddevelopment64 )
        modelList.add(myModel2)
        val myModel3 = RecyclerModel(name = "Android", profileImage =R.drawable.icons8android30 )
        modelList.add(myModel3)
        val myModel4 = RecyclerModel(name = "DBA", profileImage =R.drawable.icons8database50 )
        modelList.add(myModel4)
        val myModel5 = RecyclerModel(name = "DevOps", profileImage =R.drawable.icons8devops30 )
        modelList.add(myModel5)
        val myModel6 = RecyclerModel(name = "BlockChain", profileImage =R.drawable.icons8blockchainnewlogo64 )
        modelList.add(myModel6)
        println(modelList)
        //어뎁터인스턴스 생성 및 적용
        myRecyclerAdapter= RecyclerAdapter(this)
        myRecyclerAdapter.submitList(modelList)

        //리싸이클러뷰 설정
        roadmaprecylerveiw.apply {
            adapter=myRecyclerAdapter
        }


    }




    override fun onDestroyView() { // 프래그먼트 삭제될때 자동으로실행
        mBinding=null
        super.onDestroyView()
    }

    override fun onItemClicke(pos:Int) {
        println("_____________")
        println(pos.toString()+"번째")
        val intent = Intent(activity, DetailRoadmap::class.java)
        intent.putExtra("touchCnt",pos)
        startActivity(intent)

    }

}