package com.dvm.appd.oasis.dbg.profile.views.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.profile.viewmodel.ProfileViewModel
import com.dvm.appd.oasis.dbg.profile.viewmodel.ProfileViewModelFactory
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fra_profile.view.*

class QrDialog:DialogFragment() {

    private val profileViewModel by lazy {
        ViewModelProviders.of(this, ProfileViewModelFactory())[ProfileViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.dia_qr,container,false)
        profileViewModel.user.observe(this, Observer {
            Observable.just(it.qrCode.generateQr())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    rootView.qrCode.setImageBitmap(it)
                }
        })
        return rootView
    }

    fun String.generateQr(): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(this, BarcodeFormat.QR_CODE, 400, 400)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }
}