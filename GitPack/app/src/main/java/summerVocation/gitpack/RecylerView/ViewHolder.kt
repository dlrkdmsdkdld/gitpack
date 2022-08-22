package summerVocation.gitpack.RecylerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_recycler_item.view.*

class ViewHolder(itemView: View,recyclerViewInterfacea:RecyclerViewInterface): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    private val TitleTextView = itemView.cardViewText
    private val ImageView = itemView.cardViewImage
    private var recyclerViewInterface :RecyclerViewInterface?=null
    init {
        itemView.setOnClickListener(this)
        recyclerViewInterface=recyclerViewInterfacea
    }
    //데이터와 뷰를 바인드함
    fun bind(model: RecyclerModel){
        TitleTextView.text=model.name
        ImageView.setImageResource(model.profileImage)

    }

    override fun onClick(p0: View?) {
        this.recyclerViewInterface?.onItemClicke(adapterPosition)
    }
}
//class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding,root) {
//    //View와 데이터를 연결시키는 함수
//    fun bind(model: RecyclerModel) {
//        binding.TitleTextView.text = model.name
//        binding.ImageView.setImageResource(model.profileImage)
//    }
//}