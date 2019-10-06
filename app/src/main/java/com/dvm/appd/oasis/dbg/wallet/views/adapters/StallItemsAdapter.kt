package com.dvm.appd.oasis.dbg.wallet.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedStallItemsData
import kotlinx.android.synthetic.main.adapter_wallet_stall_items.view.*
import java.util.Collections.emptyList

class StallItemsAdapter(private val listener: StallItemsChildAdapter.OnAddClickedListener) :RecyclerView.Adapter<StallItemsAdapter.ItemsViewHolder>() {

    var items :List<Pair<String, List<ModifiedStallItemsData>>> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_wallet_stall_items,parent,false)
        return ItemsViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {

        holder.category.text = items[position].first
        holder.items.adapter = StallItemsChildAdapter(listener).apply {
            this.stallItems = items[position].second
        }
        Log.d("StallItems", items[position].second.toString())
        (holder.items.adapter as StallItemsChildAdapter).stallItems = items[position].second
        (holder.items.adapter as StallItemsChildAdapter).notifyDataSetChanged()

    }

    inner class ItemsViewHolder(view: View):RecyclerView.ViewHolder(view){

        val category: TextView = view.category
        val items: RecyclerView = view.items
    }
}