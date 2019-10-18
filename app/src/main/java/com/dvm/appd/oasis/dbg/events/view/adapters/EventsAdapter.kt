package com.dvm.appd.oasis.dbg.events.view.adapters

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.events.data.room.dataclasses.ModifiedEventsData
import kotlinx.android.synthetic.main.adapter_misc_events.view.*
import java.lang.Exception

class EventsAdapter(private val listener: OnMarkFavouriteClicked): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>(){

    var events: List<ModifiedEventsData> = emptyList()

    interface OnMarkFavouriteClicked{
        //fun updateIsFavourite(eventId: Int, favouriteMark: Int)
        fun getDirections(venue: String)
        fun showAboutRules(details: String, name: String, contact: String)
    }

    inner class EventsViewHolder(view: View): RecyclerView.ViewHolder(view){

        val event: TextView = view.eventName
        val description: TextView = view.eventDesc
        val time: TextView = view.eventTime
        val venue: TextView = view.eventVenue
        //val markFav: ImageView = view.markFav
        val directions: ImageView = view.directions
        val view: View = view.view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_misc_events, parent, false)
        return EventsViewHolder(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {


        holder.event.text = events[position].name
        holder.event.isSelected = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.description.text = Html.fromHtml(events[position].details, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }else{
            holder.description.text = events[position].details
        }
        //holder.description.text = events[position].details
        holder.time.text = events[position].time
        holder.venue.text = events[position].venue

//        if (events[position].isFav == 1){
//            holder.markFav.setBackgroundResource(R.drawable.ic_is_favourite)
//        }else if (events[position].isFav == 0){
//            holder.markFav.setBackgroundColor(R.drawable.ic_not_favourite)
//        }
//
//        holder.markFav.setOnClickListener {
//
//            if (events[position].isFav == 1){
//                listener.updateIsFavourite(events[position].eventId, 0)
//            }
//            else if (events[position].isFav == 0){
//                listener.updateIsFavourite(events[position].eventId, 1)
//            }
//        }

        holder.directions.setOnClickListener {
            listener.getDirections(events[position].venue)
        }

        holder.view.setOnClickListener {
            listener.showAboutRules(events[position].details, events[position].name, events[position].contact)
        }
    }

}