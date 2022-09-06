package summerVocation.gitpack.RecylerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import summerVocation.gitpack.R
import summerVocation.gitpack.model.SearchUser

class RecyclerAdapter: RecyclerView.Adapter<ViewHolder>() {
    private var modellist = ArrayList<SearchUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        println("onCreateViewHolder")
        val photoItemViewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_recycler_item,parent,false))
        return  photoItemViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("바인드됨")
        holder.bindWithView(this.modellist[position])

    }

    fun submitList(modelListp: ArrayList<SearchUser>) {
        this.modellist = modelListp
        println(this.modellist)
    }

    override fun getItemCount(): Int {
        return this.modellist.size
    }

}


