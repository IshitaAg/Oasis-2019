package com.dvm.appd.oasis.dbg.wallet.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedCartData
import kotlinx.android.synthetic.main.adapter_cart_item.view.*

class CartAdapter(private val listener: CartChildAdapter.OnButtonClicked, private val listener2: OrderButtonClicked): RecyclerView.Adapter<CartAdapter.CartHolder>(){

    var cartItems: List<Pair<String, List<ModifiedCartData>>> = emptyList()
    var price: Int = 0

    interface OrderButtonClicked{
        fun placeOrder()
    }

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

        if (position == cartItems.lastIndex){
            holder.price.isVisible = true
            holder.price.text = "₹ $price"
            holder.order.isVisible = true
            holder.order.isClickable = true
            holder.order.setOnClickListener {
                listener2.placeOrder()
            }
        }
        else{
            holder.price.isVisible = false
            holder.order.isVisible = false
            holder.order.isClickable = false
        }
    }

    inner class CartHolder(view: View): RecyclerView.ViewHolder(view){

        val vendor: TextView = view.vendor
        val items: RecyclerView = view.items
        val price: TextView = view.price
        val order: TextView = view.order
    }

}