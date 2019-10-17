package com.dvm.appd.oasis.dbg.profile.views.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import kotlinx.android.synthetic.main.dia_wallet_referral.view.*

class ReferralDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.dia_wallet_referral, container, false)
        val code = this.activity!!.application.getSharedPreferences("bosm.sp", Context.MODE_PRIVATE).getString(AuthRepository.Keys.referralCode, "")
        if (code != "") {
            rootView.referralCode.setText(code)
            rootView.referralCode.isEnabled = false
            rootView.shareReferral.setOnClickListener {
                var shareBody = "Referral Code = $code"
                Log.d("Profile Frag", "Message Body = ${shareBody}")
                var sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Referral Code for the Official Oasis App")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, "Share"))
                dialog!!.dismiss()
            }
        } else {
            Toast.makeText(context, "Unable to fetch Referral Code. Try again Later", Toast.LENGTH_LONG).show()
            dialog!!.dismiss()
        }

        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}