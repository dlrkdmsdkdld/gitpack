package summerVocation.gitpack.RecylerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import summerVocation.gitpack.R
import summerVocation.gitpack.model.SearchHistory

class searchHistoryAdapter(searchHistoryRecylcerview: ISearchHistoryRecylcerview): RecyclerView.Adapter<searchHistoryViewHolder>(){
    private var searchLogArray =ArrayList<SearchHistory>()
    private var iSearchHistoryRecylcerview:ISearchHistoryRecylcerview?=null
    init {
        this.iSearchHistoryRecylcerview = searchHistoryRecylcerview
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchHistoryViewHolder {
        val itemViewHolder = searchHistoryViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_search_item,parent,false),this.iSearchHistoryRecylcerview!! )
        return itemViewHolder
    }

    override fun onBindViewHolder(holder: searchHistoryViewHolder, position: Int) {
        holder.bindWithView(this.searchLogArray[position])
    }

    override fun getItemCount(): Int {
        return this.searchLogArray.size
    }
    fun submit(Log:ArrayList<SearchHistory>){
        this.searchLogArray=Log


    }
}