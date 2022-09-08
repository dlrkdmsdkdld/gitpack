package summerVocation.gitpack.RecylerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_search_item.view.*
import summerVocation.gitpack.model.SearchHistory

class searchHistoryViewHolder(itemView: View, searchRecylcerviewInterface: ISearchHistoryRecylcerview):
    RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
    //클릭할때는 생성자로 링킹처리해줘야함

    private var mySearchRecyclerViewInterface:ISearchHistoryRecylcerview
    private val timestamp = itemView.search_time_textView
    private val Log = itemView.Search_name_textView
    private val deletebtn = itemView.delete_search_btn
    private val constraintSearchItem = itemView.constraint_search_item
    init {

        //리스너 연결
        deletebtn.setOnClickListener(this)
        constraintSearchItem.setOnClickListener(this)
        this.mySearchRecyclerViewInterface=searchRecylcerviewInterface
    }
    fun bindWithView(data: SearchHistory){
        timestamp.text=data.timeStamp
        Log.text=data.term

    }

    override fun onClick(p0: View?) {
        when(p0){
            deletebtn ->{
                this.mySearchRecyclerViewInterface.onSearchItemDeleteClicked(adapterPosition)
            }
            constraintSearchItem ->{
                this.mySearchRecyclerViewInterface.onSearchItemClicked(adapterPosition)
            }
        }
    }

}