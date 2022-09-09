package summerVocation.gitpack.fragments


import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_roadmap.*
import summerVocation.gitpack.MainActivity
import summerVocation.gitpack.R
import summerVocation.gitpack.RecylerView.ISearchHistoryRecylcerview
import summerVocation.gitpack.RecylerView.RecyclerAdapter
import summerVocation.gitpack.RecylerView.searchHistoryAdapter
import summerVocation.gitpack.databinding.FragmentRoadmapBinding
import summerVocation.gitpack.model.SearchHistory
import summerVocation.gitpack.model.SearchUser
import summerVocation.gitpack.retrofit.RetrofitManager
import summerVocation.gitpack.utils.Constant.TAG
import summerVocation.gitpack.utils.PreferenceUtil
import summerVocation.gitpack.utils.RESPONSE_STATUS
import summerVocation.gitpack.utils.toSimpleString
import java.util.*


class roadMapFragment : Fragment() ,SearchView.OnQueryTextListener,ISearchHistoryRecylcerview,View.OnClickListener{
    private lateinit var myRecyclerAdapter: RecyclerAdapter // 유저데이터 들어갈 리싸이클러뷰 어뎁터
    private var mBinding : FragmentRoadmapBinding? =null
    //유저 검색 기록 배열
    private var SearchUserArrayList = ArrayList<SearchUser>()
    //서치뷰
    private lateinit var mySearchView: SearchView
    private lateinit var mySearchViewEditText:EditText
    //유저 검색 보여주는 리싸이클러뷰
    private var SearchHistoryArrayList = ArrayList<SearchHistory>()
    private lateinit var myHistoryRecyclerAdapter: searchHistoryAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRoadmapBinding.inflate(inflater,container,false)
        mBinding =binding
        Log.d(TAG,"onCreateView")
        //툴바 적용
        val myToolbar = binding.topAppBar
        (context as AppCompatActivity).setSupportActionBar(myToolbar)
        setHasOptionsMenu(true)
        val userId=(activity as MainActivity).getuserId() //메인액티비티에서 유저 아이디 가져오기

        // (getActivity() as AppCompatActivity?)!!.setSupportActionBar(topAppBar)
        this.myRecyclerAdapter = RecyclerAdapter()
        getFollower(userId)
        //검색기록 가져오기
        this.SearchHistoryArrayList=PreferenceUtil.getSearchHistoryList() as ArrayList<SearchHistory>
        this.SearchHistoryArrayList.forEach {
            Log.d(TAG,"저장된 검색기록 term : ${it.term}")
        }
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HistoryRecylcerwith(SearchHistoryArrayList)
        clear_search_history_buttton.setOnClickListener(this)
    }
    private fun HistoryRecylcerwith(List:ArrayList<SearchHistory>){
        this.myHistoryRecyclerAdapter = searchHistoryAdapter(this)
        this.myHistoryRecyclerAdapter.submit(List)
        val lm= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        my_search_history_recycler_view.apply {
            layoutManager=lm
            adapter=myHistoryRecyclerAdapter
        }
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
                mySearchViewEditText =this.findViewById(androidx.appcompat.R.id.search_src_text)
                mySearchViewEditText.apply {
                    this.filters=arrayOf(InputFilter.LengthFilter(15))
                    this.setTextColor(Color.WHITE)
                    this.setHintTextColor(Color.WHITE)
                }
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
        RetrofitManager.instance.searchUser(username = query, completion = { responseStatus, arrayList ->
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
        if (!p0.isNullOrEmpty()){
            insertSearchUserHistory(p0)
            this.SearchUserAPICall(p0)

        }

        this.mySearchView.setQuery("",false)
        this.mySearchView.clearFocus()
        this.topAppBar.collapseActionView()
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        Log.d(TAG,"onQueryTextChange :  $p0")
        return true
    }
    private fun insertSearchUserHistory(query: String){
        val indexListToRemove = ArrayList<Int>()
        this.SearchHistoryArrayList.forEachIndexed { index, searchHistory ->
            if(searchHistory.term == query){
                indexListToRemove.add(index)
            }
        }
        indexListToRemove.forEach {
            this.SearchHistoryArrayList.removeAt(it)
        }
        val newHistory = SearchHistory(term = query, timeStamp = Date().toSimpleString())
        this.SearchHistoryArrayList.add(newHistory)
        PreferenceUtil.storeSearchHistoryList(SearchHistoryArrayList)
        this.myHistoryRecyclerAdapter.notifyDataSetChanged()
    }

    override fun onSearchItemDeleteClicked(position: Int) {
       Log.d(TAG,"삭제버튼 클릭")
        this.SearchHistoryArrayList.removeAt(position)
        PreferenceUtil.storeSearchHistoryList(this.SearchHistoryArrayList)
        this.myHistoryRecyclerAdapter.notifyDataSetChanged()

    }

    override fun onSearchItemClicked(position: Int) {
        Log.d(TAG,"아이템버튼 클릭")
        SearchUserAPICall(this.SearchHistoryArrayList[position].term)
        linear_searchhistory_view.visibility=View.INVISIBLE

    }

    override fun onClick(p0: View?) {
        Log.d(TAG,"클릭한것 : $p0")
        when(p0){
            clear_search_history_buttton ->{
                Log.d(TAG,"검색기록 전체삭제")
                PreferenceUtil.clearSearchHistoryList()
                this.SearchHistoryArrayList.clear()
                linear_searchhistory_view.visibility=View.INVISIBLE

            }
        }
    }


}