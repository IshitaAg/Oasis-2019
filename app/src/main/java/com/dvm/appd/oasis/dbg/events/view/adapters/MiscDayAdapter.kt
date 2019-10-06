package com.dvm.appd.oasis.dbg.events.view.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.adapter_misc_day.view.*

class MiscDayAdapter(private val listener: OnDaySelected): RecyclerView.Adapter<MiscDayAdapter.MiscDayViewHolder>(){

    var miscDays: List<String> = emptyList()
    var daySelected: String = ""

    interface OnDaySelected{
        fun daySelected(day: String, position: Int)
    }

    inner class MiscDayViewHolder(view: View): RecyclerView.ViewHolder(view){
        val day: TextView = view.day
        val underline: View = view.underline
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiscDayViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_misc_day, parent, false)

        return MiscDayViewHolder(view)
    }

    override fun getItemCount(): Int = miscDays.size

    override fun onBindViewHolder(holder: MiscDayViewHolder, position: Int) {

        holder.day.text = miscDays[position]

        if (miscDays[position] == daySelected){
            holder.day.setTextColor(Color.rgb(104, 81, 218))
            holder.day.setTypeface(null, Typeface.BOLD)
            holder.underline.setBackgroundColor(Color.rgb(104, 81, 218))
        }
        else{
            holder.day.setTextColor(Color.rgb(137, 134, 134))
            holder.day.setTypeface(null, Typeface.NORMAL)
            holder.underline.setBackgroundColor(Color.WHITE)
        }

        holder.day.setOnClickListener {
            listener.daySelected(miscDays[position], position)
        }
    }
}