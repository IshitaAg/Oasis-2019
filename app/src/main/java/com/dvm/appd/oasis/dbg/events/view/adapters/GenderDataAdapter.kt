package com.dvm.appd.oasis.dbg.events.view.adapters

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.card_sports_horizontal.view.*

class GenderDataAdapter( private val genderDefault: String,private val listener: GenderDataAdapter.OnGenderClicked): RecyclerView.Adapter<GenderDataAdapter.GenderViewHolder>(){

    var gender: List<String> = emptyList()
    var genderSelected=genderDefault
       interface OnGenderClicked{
        fun genderClicked(gender: String)
    }

    class GenderViewHolder(view: View): RecyclerView.ViewHolder(view){

        val textView: TextView = view.editTextGender
        val parent:ConstraintLayout=view.Parent_horizontal
        val line: View = view.line
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_sports_horizontal, parent, false)
        return GenderViewHolder(view)
    }

    override fun getItemCount(): Int = gender.size

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        holder.textView.setText(gender[position])
        if (gender[position].equals(genderSelected))
        {
            holder.textView.setTextColor(Color.rgb(104, 81, 218))
            holder.textView.setTypeface(null, Typeface.BOLD)
            holder.line.setBackgroundColor(Color.rgb(104, 81, 218))
        }
        else
        {
            holder.textView.setTextColor(Color.rgb(137, 134, 134))
            holder.textView.setTypeface(null, Typeface.NORMAL)
            holder.line.setBackgroundColor(Color.WHITE)
        }


        Log.d("SportsGender","Gender onBindviewholder genderClicked$${gender[position]}")


        holder.textView.setOnClickListener {
                Log.d("SportsGender","Gender data Apadter genderClicked$${gender[position]}")
                genderSelected=gender[position]
                notifyDataSetChanged()
                listener.genderClicked(gender[position])

        }

    }

}