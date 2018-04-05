package app.parviz.com.simpleyoutubedownloader.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import app.parviz.com.simpleyoutubedownloader.R
import kotlinx.android.synthetic.main.activity_data_binding.*

class DataBindingActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by lazy { recycler }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_binding)

        val simpleItem = Item("simple item")
        val doubleItem = DoubleItem("double item 1", "double item2")
        val thirdItem = ThirdItem("advertisement")

        val adapter: MyAbstractAdapter = MyTypedAdapter(
                mutableListOf(simpleItem, doubleItem, simpleItem, thirdItem,
                        doubleItem, doubleItem, simpleItem, thirdItem, simpleItem, doubleItem, doubleItem, thirdItem),
                mutableMapOf(
                        Pair(Item::class.java, R.layout.test_item),
                        Pair(DoubleItem::class.java, R.layout.test_double_item),
                        Pair(ThirdItem::class.java, R.layout.test_third_item)
                )
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}