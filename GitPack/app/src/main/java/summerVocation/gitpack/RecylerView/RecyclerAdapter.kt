package summerVocation.gitpack.RecylerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import summerVocation.gitpack.R

class RecyclerAdapter(recyclerViewInterfacea: RecyclerViewInterface): RecyclerView.Adapter<ViewHolder>() {
    private var modellist = ArrayList<RecyclerModel>()
    private var recyclerViewInterface: RecyclerViewInterface? = null

    init {
        this.recyclerViewInterface = recyclerViewInterfacea
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        println("onCreateViewHolder")
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_recycler_item, parent, false), recyclerViewInterface!!
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("바인드됨")
        holder.bind(this.modellist[position])

    }

    fun submitList(modelListp: ArrayList<RecyclerModel>) {
        this.modellist = modelListp
        println(this.modellist)
    }

    override fun getItemCount(): Int {
        return this.modellist.size
    }

}


