package com.dvm.appd.oasis.dbg.auth.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.auth.viewmodel.AuthViewModel
import com.dvm.appd.oasis.dbg.auth.viewmodel.AuthViewModelFactory
import kotlinx.android.synthetic.main.dia_login_referral.*
import kotlinx.android.synthetic.main.dia_login_referral.view.*

class ReferralEnterDialog: DialogFragment(){

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dia_login_referral, container, false)
        authViewModel = ViewModelProviders.of(activity!!, AuthViewModelFactory())[AuthViewModel::class.java]

        (authViewModel.referralState as MutableLiveData).value = false

        view.okReferral.setOnClickListener {
            if (referralText.text.toString() == ""){
                Toast.makeText(context, "Please enter referral code", Toast.LENGTH_SHORT).show()
            }
            else{
                authViewModel.setReferral(referralText.text.toString())
                dismiss()
            }
        }

        view.cancelReferral.setOnClickListener {
            dismiss()
        }

        return view
    }
}