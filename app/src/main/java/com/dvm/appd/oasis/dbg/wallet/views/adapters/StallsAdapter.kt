package com.dvm.appd.oasis.dbg.wallet.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses.StallData
import kotlinx.android.synthetic.main.adapter_wallet_stalls.view.*

class StallsAdapter (private val listener:OnStallSelectedListener): RecyclerView.Adapter<StallsAdapter.StallsViewHolder>() {

    var stalls: List<StallData> = emptyList()
    var stallImgs:List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StallsAdapter.StallsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_wallet_stalls, parent, false)

        return StallsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stalls.size
    }

    override fun onBindViewHolder(holder: StallsViewHolder, position: Int) {
        holder.stallName.text = stalls[position].stallName

            Glide.with(holder.itemView.context!!).load(stallImgs[position]).circleCrop().placeholder(R.color.zxing_transparent).circleCrop().into(holder.stallImg)

        holder.parent.setOnClickListener {
            listener.stallSelected(stalls[position])
        }
    }
    inner class StallsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stallName = view.quantity
        val stallImg = view.sImg
        val parent = view.stallImage
    }

    interface OnStallSelectedListener{
        fun stallSelected(stall: StallData)
    }

}