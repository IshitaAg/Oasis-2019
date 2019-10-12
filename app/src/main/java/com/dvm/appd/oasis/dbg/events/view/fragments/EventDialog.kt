package com.dvm.appd.oasis.dbg.events.view.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.DialogFragment
import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.dia_event_data.view.*

class EventDialog: DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val about = arguments!!.getString("about")
        val rules = arguments!!.getString("rules")

        val view = inflater.inflate(R.layout.dia_event_data, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.about.text = Html.fromHtml(about, FROM_HTML_MODE_LEGACY)
        }else{
            view.about.text = about
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.rules.text = Html.fromHtml(rules, FROM_HTML_MODE_LEGACY)
        }else{
            view.rules.text = rules
        }

        return view
    }

}