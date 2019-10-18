package com.dvm.appd.oasis.dbg.more.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.more.dataClasses.Developer
import kotlinx.android.synthetic.main.row_developer.view.*

class DevelopersAdapter : RecyclerView.Adapter<DevelopersAdapter.DeveloperVHolder>() {

    var developers: List<Developer> = emptyList()


    override fun getItemCount() = developers.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeveloperVHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DeveloperVHolder(
            inflater.inflate(
                R.layout.row_developer,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DeveloperVHolder, position: Int) {
        val developer = developers[position]

        Glide.with(holder.itemView.context!!)
            .load(developers[position].imageLink)
            .placeholder(R.drawable.ic_person_placeholder).circleCrop()
            .into(holder.picIMG)
        holder.nameLBL.text = developer.name
        holder.roleLBL.text = developer.role

    }

    class DeveloperVHolder(rootPOV: View) : RecyclerView.ViewHolder(rootPOV) {

        val nameLBL: TextView = rootPOV.nameLBL
        val roleLBL: TextView = rootPOV.roleLBL
        val picIMG: ImageView = rootPOV.picIMG
    }
}