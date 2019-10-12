package com.dvm.appd.oasis.dbg.events.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.dia_event_data.view.*

class EventDialog: DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val about = arguments!!.getString("about")
        val rules = arguments!!.getString("rules")

        val view = inflater.inflate(R.layout.dia_event_data, container, false)

        view.about.text = about
        view.rules.text = rules

        return view
    }

}