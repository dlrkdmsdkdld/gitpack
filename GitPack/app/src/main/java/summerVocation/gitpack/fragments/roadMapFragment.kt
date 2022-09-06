package summerVocation.gitpack.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_roadmap.*
import summerVocation.gitpack.MainActivity
import summerVocation.gitpack.R
import summerVocation.gitpack.RecylerView.RecyclerAdapter
import summerVocation.gitpack.databinding.FragmentRoadmapBinding
import summerVocation.gitpack.model.SearchUser
import summerVocation.gitpack.retrofit.RetrofitManager
import summerVocation.gitpack.utils.Constant.TAG
import summerVocation.gitpack.utils.RESPONSE_STATUS


class roadMapFragment : Fragment() ,SearchView.OnQueryTextListener{
    private lateinit var myRecyclerAdapter: RecyclerAdapter // 유저데이터 들어갈 리싸이클러뷰 어뎁터
    private var mBinding : FragmentRoadmapBinding? =null
    private var SearchUserArrayList = ArrayList<SearchUser>()
    //서치뷰
    private lateinit var mySearchView: SearchView
    private lateinit var mySearchViewEditText:EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRoadmapBinding.inflate(inflater,container,false)
        mBinding =binding
        Log.d(TAG,"onCreateView")
        val myToolbar = binding.topAppBar
        (context as AppCompatActivity).setSupportActionBar(myToolbar)
        setHasOptionsMenu(true)
        val userId=(activity as MainActivity).getuserId() //메인액티비티에서 유저 아이디 가져오기

        // (getActivity() as AppCompatActivity?)!!.setSupportActionBar(topAppBar)
        this.myRecyclerAdapter = RecyclerAdapter()
        getFollower(userId)

        return mBinding?.root
    }
    private fun getFollower(userId:String){
        RetrofitManager.instance.searchFollower( username = userId, completion = { responseStatus, arrayList ->
            when(responseStatus){
                RESPONSE_STATUS.OKAY ->{
                    Log.d(TAG,"api 호출 성공 $arrayList")
                    if(arrayList!=null){
                        arrayList.forEach {
                            SearchFlowerAPICall(it)
                        }

                    }
                    roadmaprecylerveiw.apply {
                        adapter=myRecyclerAdapter
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
        Log.d(TAG,"onCreateOptionsMenu")

        this.mySearchView = menu?.findItem(R.id.search_menu_item)?.actionView as SearchView
        this.mySearchView.apply {
            this.queryHint = "유저 아이디를 입력해주세요"
            this.setOnQueryTextFocusChangeListener { view, hasExpanded ->
                when(hasExpanded){
                    true -> {
                        Log.d(TAG,"서치뷰 열림")
                        linear_searchhistory_view.visibility=View.VISIBLE

                    }
                    false ->{
                        Log.d(TAG,"서치뷰 닫힘")
                        linear_searchhistory_view.visibility=View.INVISIBLE

                    }
                }
                this.setOnQueryTextListener(this@roadMapFragment)

            }
        }
    }


    private fun SearchFlowerAPICall(query:String){
        RetrofitManager.instance.searchUser(username = query, completion = { responseStatus, arrayList ->
            when(responseStatus){
                RESPONSE_STATUS.OKAY ->{
                    Log.d(TAG,"api 호출 성공 $arrayList")
                    if(arrayList!=null){
                        this.SearchUserArrayList.clear()
                        this.myRecyclerAdapter.addList(arrayList)
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
    private fun SearchUserAPICall(query:String){
        RetrofitManager.instance.searchUser(username = "query", completion = { responseStatus, arrayList ->
            when(responseStatus){
                RESPONSE_STATUS.OKAY ->{
                    Log.d(TAG,"api 호출 성공 $arrayList")
                    if(arrayList!=null){
                        val tmp = ArrayList<SearchUser>()
                        tmp.add(arrayList)
                        this.SearchUserArrayList.clear()
                        this.SearchUserArrayList=tmp
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

    override fun onQueryTextSubmit(p0: String?): Boolean {
        Log.d(TAG,"onQueryTextSubmit :  $p0")
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        Log.d(TAG,"onQueryTextChange :  $p0")
        return true
    }


}