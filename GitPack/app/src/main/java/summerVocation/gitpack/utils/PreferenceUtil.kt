package summerVocation.gitpack.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import summerVocation.gitpack.MyApplication
import summerVocation.gitpack.model.SearchHistory
import summerVocation.gitpack.utils.Constant.TAG

object PreferenceUtil{
    private const val SHARED_SHEARCH_HISTORY="shared_search_history"
    private const val KEY_SEARCH_HISTORY = "key_search_history"

    //검색목록을 저장
    fun storeSearchHistoryList(searchHistoryList:MutableList<SearchHistory>){
        Log.d(TAG,"SharedPrefManager -storeSearchHistoryList() called")
        //매개변수로 들어온 배열을 -> json 문자열로 변환
        val searchHistoryListString : String= Gson().toJson(searchHistoryList)
        Log.d(TAG,"SharedPrefManager -searchHistoryListString: $searchHistoryListString")
        //쉐어드 가져오기 - 원래있던거 가져와서 거기에 추가하려고 그러는거같음
        val shared = MyApplication.instance.getSharedPreferences(SHARED_SHEARCH_HISTORY, Context.MODE_PRIVATE)

        //쉐어드 에디터 가져오기  수정을 하겠다는 뜻
        val editor = shared.edit()
        editor.putString(KEY_SEARCH_HISTORY,searchHistoryListString)
        editor.apply()
    }
    fun getSearchHistoryList() : MutableList<SearchHistory>{
        //저장했던 쉐어드
        val shared = MyApplication.instance.getSharedPreferences(SHARED_SHEARCH_HISTORY, Context.MODE_PRIVATE)
        val storedSearchHistoryListString= shared.getString(KEY_SEARCH_HISTORY,"")!!//없다면 널이라고하겠다
        var storedSearchHistoryList = ArrayList<SearchHistory>()
        if (storedSearchHistoryListString.isNotEmpty()){
            //저장된 문자열(json형태로 저장되어있는)을 객체배열로 변경
            storedSearchHistoryList = Gson()
                .fromJson(storedSearchHistoryListString , Array<SearchHistory>::class.java)
                .toMutableList() as ArrayList<SearchHistory>
        }
        return storedSearchHistoryList
    }
    //검색목록 지우기
    fun clearSearchHistoryList(){
        val shared = MyApplication.instance.getSharedPreferences(SHARED_SHEARCH_HISTORY, Context.MODE_PRIVATE)

        //쉐어드 에디터 가져오기  수정을 하겠다는 뜻
        val editor = shared.edit()
        editor.clear()
        editor.apply()
    }




}