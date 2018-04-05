package app.parviz.com.simpleyoutubedownloader.test

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import app.parviz.com.simpleyoutubedownloader.BR

class MyViewHolder(private var itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(obj: Any) {
        itemBinding.setVariable(BR.item, obj)
        itemBinding.executePendingBindings()
    }
}