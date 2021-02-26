package snnafi.bangla.dictionary.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import snnafi.bangla.dictionary.admin.R

class UniversalAdapter(val itemOne: List<String>, val itemTwo: List<Int>) : BaseAdapter() {

    override fun getCount(): Int {
        return itemOne.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(p2?.context).inflate(R.layout.item_list, p2, false)
        val textView = view.findViewById<TextView>(R.id.itemName)
        textView.setText(itemOne[p0])
        return view

    }

}