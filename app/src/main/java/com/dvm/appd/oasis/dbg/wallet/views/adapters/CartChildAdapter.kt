package com.dvm.appd.oasis.dbg.wallet.views.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedCartData
import kotlinx.android.synthetic.main.adapter_cart_item_child.view.*

class CartChildAdapter(private val listener: OnButtonClicked): RecyclerView.Adapter<CartChildAdapter.CartViewHolder>(){

    var cartChildItems: List<ModifiedCartData> = emptyList()

    interface OnButtonClicked{

        fun plusButtonClicked(item: ModifiedCartData, quantity: Int)
        fun deleteCartItemClicked(itemId: Int)
    }

    inner class CartViewHolder(view: View): RecyclerView.ViewHolder(view){

        val itemName: TextView = view.itemName
        val basePrice: TextView = view.basePrice
        val quantity: TextView = view.quantity
        val plus: Button = view.plus
        val minus: Button = view.minus
        val isVeg: ImageView = view.isVeg
        val discount: TextView = view.discount
        val currentPrice: TextView = view.currentPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_cart_item_child, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartChildItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        Log.d("CartRecycler", "$cartChildItems")
        holder.itemName.text = cartChildItems[position].itemName

        if (cartChildItems[position].discount == 0){
            holder.currentPrice.isVisible = true
            holder.discount.isVisible = true
            holder.basePrice.text = "₹ ${cartChildItems[position].quantity * cartChildItems[position].basePrice}"
            holder.basePrice.paintFlags = holder.basePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.currentPrice.text = "₹ ${cartChildItems[position].quantity * cartChildItems[position].currentPrice}"
            holder.discount.text = "~ ${cartChildItems[position].discount}%"
        }
        else{
            holder.currentPrice.isVisible = false
            holder.discount.isVisible = false
            holder.basePrice.text = "₹ ${cartChildItems[position].quantity * cartChildItems[position].basePrice}"
            holder.basePrice.paintFlags = holder.basePrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.quantity.text = cartChildItems[position].quantity.toString()

        if (cartChildItems[position].isVeg){
            holder.isVeg.setImageResource(R.drawable.ic_veg)
        }
        else{
            holder.isVeg.setImageResource(R.drawable.ic_non_veg)
        }

        holder.plus.setOnClickListener {

            listener.plusButtonClicked(cartChildItems[position], cartChildItems[position].quantity + 1)
        }

        holder.minus.setOnClickListener {

            if (cartChildItems[position].quantity > 1) {

                listener.plusButtonClicked(cartChildItems[position], cartChildItems[position].quantity - 1)
            } else {

                listener.deleteCartItemClicked(cartChildItems[position].itemId)
            }
        }
    }
}