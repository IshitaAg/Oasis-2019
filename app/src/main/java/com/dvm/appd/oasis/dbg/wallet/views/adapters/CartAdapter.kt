package com.dvm.appd.oasis.dbg.wallet.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedCartData
import kotlinx.android.synthetic.main.adapter_cart_item.view.*

class CartAdapter(private val listener: CartChildAdapter.OnButtonClicked): RecyclerView.Adapter<CartAdapter.CartHolder>(){

    var cartItems: List<Pair<String, List<ModifiedCartData>>> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_cart_item, parent, false)
        return CartHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartHolder, position: Int) {

        holder.vendor.text = cartItems[position].first
        holder.items.adapter = CartChildAdapter(listener).apply {
            this.cartChildItems = cartItems[position].second
        }
        (holder.items.adapter as CartChildAdapter).cartChildItems = cartItems[position].second
        (holder.items.adapter as CartChildAdapter).notifyDataSetChanged()
    }

    inner class CartHolder(view: View): RecyclerView.ViewHolder(view){

        val vendor: TextView = view.vendor
        val items: RecyclerView = view.items
    }

}