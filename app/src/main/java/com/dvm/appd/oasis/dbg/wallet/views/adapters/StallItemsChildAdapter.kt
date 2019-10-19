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
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.ModifiedStallItemsData
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.adapter_wallet_stall_items_child.view.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class StallItemsChildAdapter(private val listener:OnAddClickedListener) : RecyclerView.Adapter<StallItemsChildAdapter.ChildItemsViewHolder>() {

    var stallItems :List<ModifiedStallItemsData> = emptyList()

    interface OnAddClickedListener{
        fun addButtonClicked(stallItem: ModifiedStallItemsData, quantity: Int)
        fun deleteCartItemClicked(itemId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_wallet_stall_items_child,parent,false)
        return ChildItemsViewHolder(view)
    }

    override fun getItemCount(): Int = stallItems.size

    override fun onBindViewHolder(holder: ChildItemsViewHolder, position: Int) {

        Log.d("StallItemsChild", stallItems.toString())

        holder.itemName.text = stallItems[position].itemName

        if (stallItems[position].discount != 0){
            holder.currentPrice.isVisible = true
            holder.discount.isVisible = true
            holder.basePrice.text = "₹ ${stallItems[position].basePrice}"
            holder.basePrice.paintFlags = holder.basePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.currentPrice.text = "₹ ${stallItems[position].currentPrice}"
            holder.discount.text = "(${stallItems[position].discount}% off)"
        }
        else{
            holder.basePrice.text = "₹ ${stallItems[position].basePrice}"
            holder.basePrice.paintFlags = holder.basePrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.currentPrice.isVisible = false
            holder.discount.isVisible = false
        }

        holder.quantity.text = stallItems[position].quantity.toString()

        if (stallItems[position].isVeg){
            holder.isVeg.setImageResource(R.drawable.ic_veg)
        }
        else{
            holder.isVeg.setImageResource(R.drawable.ic_non_veg)
        }

        if (stallItems[position].quantity > 0){

            holder.quantity.isEnabled = true
            holder.quantity.isVisible = true
            holder.plus.isEnabled = true
            holder.plus.isVisible = true
            holder.minus.isEnabled = true
            holder.minus.isVisible = true
            holder.add.isEnabled = false
            holder.add.isVisible = false
            holder.quantity.text = stallItems[position].quantity.toString()

            RxView.clicks(holder.plus).debounce(200, TimeUnit.MILLISECONDS).observeOn(
                AndroidSchedulers.mainThread()).subscribe {
                listener.addButtonClicked(stallItems[position], stallItems[position].quantity + 1)
            }

            RxView.clicks(holder.minus).debounce(200, TimeUnit.MILLISECONDS).subscribe {
                if (stallItems[position].quantity > 1){

                    listener.addButtonClicked(stallItems[position], stallItems[position].quantity - 1)
                }
                else{

                    listener.deleteCartItemClicked(stallItems[position].itemId)
                }
            }

        }
        else {

            holder.quantity.isEnabled = false
            holder.quantity.isVisible = false
            holder.plus.isEnabled = false
            holder.plus.isVisible = false
            holder.minus.isEnabled = false
            holder.minus.isVisible = false
            holder.add.isEnabled = true
            holder.add.isVisible = true

            RxView.clicks(holder.add).debounce(200, TimeUnit.MILLISECONDS).subscribe {
                listener.addButtonClicked(stallItems[position], 1)
            }
        }
    }

    inner class ChildItemsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val itemName: TextView = view.itemName
        val basePrice: TextView = view.basePrice
        val add: TextView = view.addBtn
        val plus: Button = view.plus
        val minus: Button = view.minus
        val quantity: TextView = view.quantity
        val isVeg: ImageView = view.isVeg
        val discount: TextView = view.discount
        val currentPrice: TextView = view.currentPrice
    }
}