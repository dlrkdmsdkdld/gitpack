package summerVocation.gitpack.RecylerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_recycler_item.view.*

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val TitleTextView = itemView.cardViewText
    private val ImageView = itemView.cardViewImage
    init {

    }
    //데이터와 뷰를 바인드함
    fun bind(model: RecyclerModel){
        TitleTextView.text=model.name
        ImageView.setImageResource(model.profileImage)

    }
}
//class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding,root) {
//    //View와 데이터를 연결시키는 함수
//    fun bind(model: RecyclerModel) {
//        binding.TitleTextView.text = model.name
//        binding.ImageView.setImageResource(model.profileImage)
//    }
//}