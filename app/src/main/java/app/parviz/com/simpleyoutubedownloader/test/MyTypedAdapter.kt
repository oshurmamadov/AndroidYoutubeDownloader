package app.parviz.com.simpleyoutubedownloader.test

class MyTypedAdapter(private var items: List<Any>, private var layoutTypes: MutableMap<Class<*>, Int>) : MyAbstractAdapter() {

    override fun getItemCount(): Int = items.size

    override fun getObjForPosition(position: Int): Any = items[position]

    override fun getLayoutIdForPosition(position: Int): Int {
        val obj = getObjForPosition(position)
        return layoutTypes[obj.javaClass] ?: throw RuntimeException("Crazy ? It cant be null, DOH !" + obj)
    }
}