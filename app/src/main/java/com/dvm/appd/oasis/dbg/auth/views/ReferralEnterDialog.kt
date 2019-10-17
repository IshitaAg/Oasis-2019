package com.dvm.appd.oasis.dbg.auth.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.auth.viewmodel.AuthViewModel
import com.dvm.appd.oasis.dbg.auth.viewmodel.AuthViewModelFactory

class ReferralEnterDialog: DialogFragment(){

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.dia_login_referral, container, false)
        authViewModel = ViewModelProviders.of(parentFragment!!, AuthViewModelFactory())[AuthViewModel::class.java]

        (authViewModel.referralState as MutableLiveData).value = false
//        onOkClick
        authViewModel.setReferral("")
        dismiss()

        //onCancel
        dismiss()

        return view
    }
}