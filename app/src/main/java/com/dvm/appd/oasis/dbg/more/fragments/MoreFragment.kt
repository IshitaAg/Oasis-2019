package com.dvm.appd.oasis.dbg.more.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.PaytmLogs

import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.more.adapters.MoreAdapter
import com.labo.kaji.fragmentanimations.FlipAnimation
import com.labo.kaji.fragmentanimations.MoveAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment(), MoreAdapter.onMoreItemClicked {
    override fun onSecretFlowEnabled() {
        // Toast.makeText(context, "Entered Secret flow", Toast.LENGTH_LONG).show()
        val editView = EditText(context)
        editView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Enter Password")
        builder.setView(editView)
        builder.setPositiveButton("Continue", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if(editView.text.toString() == "001103020011") {
                    startActivity(Intent(context, PaytmLogs::class.java))
                }
                dialog!!.dismiss()
            }

        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.dismiss()
            }
        })
        builder.show()
    }

    var moreItems = listOf("Contact Us", "Developers","Kind Store","Map","N2O Voting","EPC Blog", "HPC Blog", "Sponsors", "Terms And Conditions", "Privacy Policy")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_card_more.adapter = MoreAdapter(this)
        (recycler_card_more.adapter as MoreAdapter).moreItems = moreItems
        (recycler_card_more.adapter as MoreAdapter).notifyDataSetChanged()

    }

    override fun moreButtonClicked(item: Int) {
        // Toast.makeText(context, "Position clicked = $item.", Toast.LENGTH_LONG).show()
        when(item) {
            0 -> {
                val bundle = bundleOf("title" to "Contact Us")
                view!!.findNavController().navigate(R.id.action_action_more_to_fragmentRecyclerView, bundle)
            }
            1 -> {
                val bundle = bundleOf("title" to "Developers")
                view!!.findNavController().navigate(R.id.action_action_more_to_fragmentRecyclerView, bundle)
            }
            2->{
                view!!.findNavController().navigate(R.id.action_action_more_to_kindItemsFragment)
            }
            3 -> {
                view!!.findNavController().navigate(R.id.action_action_more_to_mapFragment)
            }
            4 -> {
                view!!.findNavController().navigate(R.id.action_action_more_to_votingFragment)
            }
            5 -> {
                val bundle = bundleOf("link" to view!!.resources.getString(R.string.link_EPC), "title" to "EPC Blog")
                view!!.findNavController().navigate(R.id.action_action_more_to_fragmentWebPage, bundle)
            }
            6 -> {
                val bundle = bundleOf("link" to view!!.resources.getString(R.string.link_HPC), "title" to "HPC Blog")
                view!!.findNavController().navigate(R.id.action_action_more_to_fragmentWebPage, bundle)
            }
            7 -> {
                val bundle = bundleOf("link" to view!!.resources.getString(R.string.link_Sponsors), "title" to "Sponsors")
                view!!.findNavController().navigate(R.id.action_action_more_to_fragmentWebPage, bundle)
            }
            8 -> {
                val bundle = bundleOf("link" to "www.bits-oasis.org/android/policy.html", "title" to "Privacy Policy")
                view!!.findNavController().navigate(R.id.action_action_more_to_fragmentWebPage, bundle)
            }
            9 -> {
                val bundle = bundleOf("link" to "www.bits-oasis.org/android/tc.html", "title" to "Terms and Conditions")
                view!!.findNavController().navigate(R.id.action_action_more_to_fragmentWebPage, bundle)
            }
        }
    }

    override fun onResume() {
        (activity!! as MainActivity).showCustomToolbar()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_more)

        super.onResume()
    }

    /*override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return MoveAnimation.create(MoveAnimation.RIGHT,true, 500)
    }*/
}
