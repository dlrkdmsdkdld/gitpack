package summerVocation.gitpack.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_roadmap.*
import summerVocation.gitpack.RecylerView.RecyclerAdapter
import summerVocation.gitpack.databinding.FragmentRoadmapBinding
import summerVocation.gitpack.model.SearchUser
import summerVocation.gitpack.retrofit.RetrofitManager
import summerVocation.gitpack.utils.Constant.TAG
import summerVocation.gitpack.utils.RESPONSE_STATUS

class roadMapFragment : Fragment() {
    private lateinit var myRecyclerAdapter: RecyclerAdapter // 유저데이터 들어갈 리싸이클러뷰 어뎁터
    private var mBinding : FragmentRoadmapBinding? =null
    private var SearchUserArrayList = ArrayList<SearchUser>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRoadmapBinding.inflate(inflater,container,false)
//        SearchUserAPICall("Yunjung324")
        mBinding =binding
        this.myRecyclerAdapter = RecyclerAdapter()
        RetrofitManager.instance.searchUser(username = "Yunjung324", completion = { responseStatus, arrayList ->
            when(responseStatus){
                RESPONSE_STATUS.OKAY ->{
                    Log.d(TAG,"api 호출 성공 $arrayList")
                    if(arrayList!=null){
                        this.SearchUserArrayList.clear()
                        this.SearchUserArrayList=arrayList
                        Log.d(TAG,"어레이리스트 연결 $SearchUserArrayList")

                        this.myRecyclerAdapter.submitList(this.SearchUserArrayList)
                        // this.myRecyclerAdapter.notifyDataSetChanged()
                        roadmaprecylerveiw.apply {
                            adapter=myRecyclerAdapter
                        }
                    }

                }
                RESPONSE_STATUS.FAIL -> {
                    Log.d(TAG, "api 호출 실패패 : $arrayList ")
                }
                RESPONSE_STATUS.NO_CONTENT -> {
                }
            }
        })
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        var modelList = ArrayList<RecyclerModel>()
//        val myModel = RecyclerModel(name = "frontend", profileImage = R.drawable.icons8frontend2 )
//        modelList.add(myModel)
//        val myModel2 = RecyclerModel(name = "Backend", profileImage =R.drawable.icons8backenddevelopment64 )
//        modelList.add(myModel2)
//        val myModel3 = RecyclerModel(name = "Android", profileImage =R.drawable.icons8android30 )
//        modelList.add(myModel3)
//        val myModel4 = RecyclerModel(name = "DBA", profileImage =R.drawable.icons8database50 )
//        modelList.add(myModel4)
//        val myModel5 = RecyclerModel(name = "DevOps", profileImage =R.drawable.icons8devops30 )
//        modelList.add(myModel5)
//        val myModel6 = RecyclerModel(name = "BlockChain", profileImage =R.drawable.icons8blockchainnewlogo64 )
//        modelList.add(myModel6)
//        println(modelList)
//        //어뎁터인스턴스 생성 및 적용
//        myRecyclerAdapter= RecyclerAdapter(this)
//        myRecyclerAdapter.submitList(modelList)
//
//        //리싸이클러뷰 설정
//        roadmaprecylerveiw.apply {
//            adapter=myRecyclerAdapter
//        }

//        roadmaprecylerveiw.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        Log.d(TAG,"myRecyclerAdapter 연결 $myRecyclerAdapter")
        Log.d(TAG,"myRecyclerAdapter 연결 $myRecyclerAdapter")
        Log.d(TAG,"myRecyclerAdapter 연결 $myRecyclerAdapter")
        //roadmaprecylerveiw.adapter=this.myRecyclerAdapter


    }

    private fun SearchUserAPICall(query:String){
        RetrofitManager.instance.searchUser(username = "Yunjung324", completion = { responseStatus, arrayList ->
            when(responseStatus){
                RESPONSE_STATUS.OKAY ->{
                    Log.d(TAG,"api 호출 성공 $arrayList")
                    if(arrayList!=null){
                        this.SearchUserArrayList.clear()
                        this.SearchUserArrayList=arrayList
                        this.myRecyclerAdapter.submitList(this.SearchUserArrayList)
                        this.myRecyclerAdapter.notifyDataSetChanged()
                    }

                }
                RESPONSE_STATUS.FAIL -> {
                    Log.d(TAG, "api 호출 실패패 : $arrayList ")
                }
                RESPONSE_STATUS.NO_CONTENT -> {
                }
            }
        })
    }


    override fun onDestroyView() { // 프래그먼트 삭제될때 자동으로실행
        mBinding=null
        super.onDestroyView()
    }



}