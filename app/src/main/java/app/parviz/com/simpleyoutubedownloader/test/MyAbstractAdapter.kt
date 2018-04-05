package app.parviz.com.simpleyoutubedownloader.test

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class MyAbstractAdapter: RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent!!.context)
        val itemBinding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val obj : Any = getObjForPosition(position)
        holder!!.bind(obj)
    }

    override fun getItemViewType(position: Int): Int = getLayoutIdForPosition(position)

    protected abstract fun getObjForPosition(position: Int): Any

    protected abstract fun getLayoutIdForPosition(position: Int): Int
}