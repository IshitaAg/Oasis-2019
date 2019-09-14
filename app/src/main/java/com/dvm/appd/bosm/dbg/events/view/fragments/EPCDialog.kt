package com.dvm.appd.bosm.dbg.events.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dvm.appd.bosm.dbg.R
import kotlinx.android.synthetic.main.dia_epc.view.*

class EPCDialog: DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val description = arguments!!.getString("description")
        val link = arguments!!.getString("link")

        val view = inflater.inflate(R.layout.dia_epc, container, false)

        view.textView19.text = description
        view.more.setOnClickListener {
            val intent = Intent("android.intent.action.VIEW", Uri.parse(link))
            startActivity(intent)
        }

        return view
    }
}