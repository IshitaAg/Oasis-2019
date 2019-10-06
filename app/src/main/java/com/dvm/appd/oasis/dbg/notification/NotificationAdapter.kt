package com.dvm.appd.oasis.dbg.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.card_notification.view.*

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var notifications: List<Notification> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotificationViewHolder(inflater.inflate(R.layout.card_notification ,parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.body.text = notifications[position].body
        holder.title.text = notifications[position].body
    }

    inner class NotificationViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title = view.TitleNotification
        val body = view.BodyNotification
    }
}