package com.dvm.appd.oasis.dbg.events.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.FilterData
import kotlinx.android.synthetic.main.adapter_filter_item.view.*

class FilterAdapter(private val listener: DialogItemTouch): RecyclerView.Adapter<FilterAdapter.FilterViewHolder>(){

    var categories: List<FilterData> = emptyList()

    interface DialogItemTouch{
        fun updateFilter(category: String, filter: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_filter_item, parent, false)
        return FilterViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {

        holder.filter.text = categories[position].category

        holder.filter.setOnClickListener {
            listener.updateFilter(categories[position].category, !categories[position].filtered)
        }
    }

    inner class FilterViewHolder(view: View): RecyclerView.ViewHolder(view){

        val filter: TextView = view.filterName
    }
}