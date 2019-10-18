package com.dvm.appd.oasis.dbg.events.view.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.DialogFragment
import com.dvm.appd.oasis.dbg.R
import kotlinx.android.synthetic.main.dia_event_data.view.*

class EventDialog: DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        val detail = arguments!!.getString("details")
        val name = arguments!!.getString("name")
        val contact = arguments!!.getString("contact")

        val view = inflater.inflate(R.layout.dia_event_data, container, false)

        view.heading.text = name

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.description.text = Html.fromHtml(detail, FROM_HTML_MODE_LEGACY)
        }else{
            view.description.text = detail
        }


        if (contact != "NA"){
            view.textView9.visibility = View.VISIBLE
            view.rules.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.rules.text = Html.fromHtml(contact, FROM_HTML_MODE_LEGACY)
            }else{
                view.rules.text = contact
            }
        }
        else{
            view.textView9.visibility = View.INVISIBLE
            view.rules.visibility = View.INVISIBLE
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            view.description.text = Html.fromHtml(description, FROM_HTML_MODE_LEGACY)
//        }else{
//            view.description.text = description
//        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

}