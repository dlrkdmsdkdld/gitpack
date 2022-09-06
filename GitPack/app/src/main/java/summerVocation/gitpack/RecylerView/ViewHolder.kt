package summerVocation.gitpack.RecylerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_recycler_item.view.*
import summerVocation.gitpack.MyApplication
import summerVocation.gitpack.R
import summerVocation.gitpack.model.SearchUser

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
     {

    private val card_View_Image = itemView.card_View_Image
    private val card_createdAt = itemView.card_createdAt
    private val card_follow_count = itemView.card_follow_count
    private val card_user_id = itemView.card_user_id
    private val card_last_commit_text = itemView.card_last_commit_text
    private var recyclerViewInterface :RecyclerViewInterface?=null

    //데이터와 뷰를 바인드함
    fun bindWithView(model: SearchUser){
        card_createdAt.text = model.createdAt
        card_follow_count.text = model.follower.toString()
        card_user_id.text=model.name
        card_last_commit_text.text=model.lastcommit.toString()

        Glide.with(MyApplication.instance)
            .load(model.imageurl)
            .placeholder(R.drawable.icons8frontend2)
            .into(card_View_Image)
    }


}
//class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding,root) {
//    //View와 데이터를 연결시키는 함수
//    fun bind(model: RecyclerModel) {
//        binding.TitleTextView.text = model.name
//        binding.ImageView.setImageResource(model.profileImage)
//    }
//}