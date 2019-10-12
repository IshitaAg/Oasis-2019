package com.dvm.appd.oasis.dbg.wallet.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.adapter_kind_items.view.*

class KindItemsAdapter() : RecyclerView.Adapter<KindItemsAdapter.KindViewHolder>() {

    var items: List<String> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KindItemsAdapter.KindViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_kind_items, parent, false)
        return KindViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: KindItemsAdapter.KindViewHolder, position: Int) {
       holder.itemName.text = items[position]
    }

    inner class KindViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       val itemName = view.ItemName
    }
}