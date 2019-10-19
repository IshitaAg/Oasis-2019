package com.dvm.appd.oasis.dbg.profile.views.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
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
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.dia_wallet_referral.view.*
import kotlinx.android.synthetic.main.fra_profile.*
import kotlinx.android.synthetic.main.fra_profile.view.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class ReferralDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.dia_wallet_referral, container, false)
        val code = this.activity!!.application.getSharedPreferences("bosm.sp", Context.MODE_PRIVATE).getString(AuthRepository.Keys.referralCode, "")
        if (code != "") {
            rootView.referralCode.setText(code)
            rootView.referralCode.isEnabled = false
            RxView.clicks(rootView.shareReferral).debounce(200, TimeUnit.MILLISECONDS).observeOn(
                AndroidSchedulers.mainThread()).subscribe {
                (parentFragment!!.swipeProfile).progress_profile.visibility = View.VISIBLE
                var shareBody ="Join me on Official Oasis'19 App, a secure app with in-built wallet," +
                        " live event tracking etc and experience this Oasis like never before.\n\n" +
                        "Android app link: https://play.google.com/store/apps/details?id=v2015.oasis.pilani.bits.com.home\n\n" +
                        "iOS app link: https://apps.apple.com/us/app/oasis-2019/id1483415633?ls=1\n\n" +
                        "Enter my code $code and earn cashback upto Rs 100 on your first order."

                Log.d("Profile Frag", "Message Body = ${shareBody}")
                var sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Referral Code for the Official Oasis App")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, "Share"))
                //(parentFragment!!.view as ProfileFragment).progress_profile.visibility = View.INVISIBLE
                (parentFragment!!.swipeProfile).progress_profile.visibility = View.INVISIBLE
                dialog!!.dismiss()
            }
           // (parentFragment!!.view as ProfileFragment).progress_profile.visibility = View.VISIBLE
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