package com.dvm.appd.oasis.dbg.more.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.card_recycler_more.view.*
import android.view.MotionEvent
import android.content.Context.VIBRATOR_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.os.Vibrator
import androidx.core.os.HandlerCompat.postDelayed
import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.content.Context
import android.os.Handler
import androidx.core.os.postDelayed


class MoreAdapter(private val listener: onMoreItemClicked) : RecyclerView.Adapter<MoreAdapter.moreViewHolder>() {

    var moreItems : List<String> = emptyList()

    interface onMoreItemClicked {
        fun moreButtonClicked(item: Int)
        fun onSecretFlowEnabled()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): moreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.dvm.appd.oasis.dbg.R.layout.card_recycler_more,parent,false)
        return moreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moreItems.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: moreViewHolder, position: Int) {
        holder.title.text = moreItems[position]
        holder.parent.setOnClickListener {
            it.isClickable = false
            listener.moreButtonClicked(position)
        }
        holder.imgBttn.setOnClickListener {
            it.isClickable = false
            holder.parent.isClickable = false
            listener.moreButtonClicked(position)
        }
        if (position == 1) {
            var isLongPress = false
            val longClickDuration: Long = 3000
            holder.parent.setOnTouchListener { v, event ->
                if (event.action === MotionEvent.ACTION_DOWN) {
                    isLongPress = true
                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        if (isLongPress) {
                           listener.onSecretFlowEnabled()
                            // set your code here
                            // Don't forgot to add <uses-permission android:name="android.permission.VIBRATE" /> to vibrate.
                        }
                    }, longClickDuration)
                } else if (event.action === MotionEvent.ACTION_UP) {
                    isLongPress = false
                }
                true
            }
        }
    }

    inner class moreViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var title = view.text_card_more_title
        var imgBttn = view.imgBttn_card_more_next
        var parent = view.linear_card_more_parent
    }
}