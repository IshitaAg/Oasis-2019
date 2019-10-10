package com.dvm.appd.oasis.dbg.wallet.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
        val quantityPrice: TextView = view.price
        val quantity: TextView = view.quantity
        val plus: Button = view.plus
        val minus: Button = view.minus
        val isVeg: ImageView = view.isVeg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_cart_item_child, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartChildItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        Log.d("CartRecycler", "$cartChildItems")
        holder.itemName.text = cartChildItems[position].itemName
        holder.quantityPrice.text = "₹ ${cartChildItems[position].quantity * cartChildItems[position].price}"
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