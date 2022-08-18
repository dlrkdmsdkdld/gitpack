package summerVocation.gitpack.RecylerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import summerVocation.gitpack.R

class RecyclerAdapter: RecyclerView.Adapter<ViewHolder>() {
    private var modellist = ArrayList<RecyclerModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        println("onCreateViewHolder")
        return  ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_recycler_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("바인드됨")
        holder.bind(this.modellist[position])
        //클릭 리스너 설정
        holder.itemView.setOnClickListener {
            
        }
    }
    fun submitList(modelListp: ArrayList<RecyclerModel>){
        this.modellist = modelListp
        println(this.modellist)
    }

    override fun getItemCount(): Int {
        return this.modellist.size
    }


}