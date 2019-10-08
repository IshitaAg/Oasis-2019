package com.dvm.appd.oasis.dbg.profile.views.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.auth.views.AuthActivity
import com.dvm.appd.oasis.dbg.profile.viewmodel.ProfileViewModel
import com.dvm.appd.oasis.dbg.profile.viewmodel.ProfileViewModelFactory
import com.dvm.appd.oasis.dbg.profile.views.adapters.UserTicketsAdapter
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dia_wallet_send_money.view.*
import kotlinx.android.synthetic.main.fra_auth_outstee.view.*
import kotlinx.android.synthetic.main.fra_profile.view.*
import kotlinx.android.synthetic.main.fra_profile.view.userId
import kotlinx.android.synthetic.main.fra_profile.view.username

class ProfileFragment : Fragment(), PaytmPaymentTransactionCallback {

    private val profileViewModel by lazy {
        ViewModelProviders.of(this, ProfileViewModelFactory())[ProfileViewModel::class.java]
    }

    //use prodPgService when production level
    private val stagingPgService = PaytmPGService.getStagingService()
    private val prodPgService = PaytmPGService.getProductionService()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fra_profile, container, false)
        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_profile)

        rootView.logout.setOnClickListener {
            profileViewModel.logout()
        }

        profileViewModel.balance.observe(this, Observer {
            rootView.balance.text = "Rs ${it!!}"
        })

        rootView.qrCode.setOnClickListener {
            QrDialog().show(childFragmentManager,"QR_DIALOG")
        }


        rootView.AddBtn.setOnClickListener {
            AddMoneyDialog().show(childFragmentManager,"ADD_MONEY_DIALOG")
        }


        rootView.sendBtn.setOnClickListener {
            SendMoneyDialog().show(childFragmentManager,"SEND_MONEY_DIALOG")
        }

        rootView.buyTicket.setOnClickListener {
            TicketDialog().show(childFragmentManager,"TICKETS_DIALOG")

        }


        profileViewModel.order.observe(this, Observer {
            when (it!!) {
                UiState.MoveToLogin -> {
                    activity!!.finishAffinity()
                    startActivity(Intent(context!!, AuthActivity::class.java))
                }
                UiState.ShowIdle -> {
                    rootView.loading.visibility = View.GONE
                }
                UiState.ShowLoading -> {
                    rootView.loading.visibility = View.VISIBLE
                }
            }
        })

        profileViewModel.user.observe(this, Observer {
            rootView.username.text = it.name
            rootView.userId.text = "User id: ${it.userId}"
            Observable.just(it.qrCode.generateQr())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    rootView.qrCode.setImageBitmap(it)
                }
        })


        rootView.userTickets.adapter = UserTicketsAdapter()
        profileViewModel.userTickets.observe(this, Observer {
            Log.d("TicketsUserP", "$it")
            (rootView.userTickets.adapter as UserTicketsAdapter).userTickets = it
            (rootView.userTickets.adapter as UserTicketsAdapter).notifyDataSetChanged()
        })

        profileViewModel.error.observe(this, Observer {
            if (it != null){
                Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
                (profileViewModel.error as MutableLiveData).postValue(null)
            }
        })


        //Add some button for paytm
//        rootView.addBtn.setOnClickListener {
//            profileViewModel.getCheckSum(stagingPgService, prodPgService, this, "1000")
//        }

        return rootView
    }

    fun String.generateQr(): Bitmap {
        val bitMatrix = MultiFormatWriter().encode(this, BarcodeFormat.QR_CODE, 400, 400)
        return BarcodeEncoder().createBitmap(bitMatrix)
    }

    override fun onTransactionResponse(p0: Bundle?) {
        Log.d("PaytmTrans", p0.toString())
    }

    override fun clientAuthenticationFailed(p0: String?) {
        Log.d("PaytmAuthFailed", "Network Error")
    }

    override fun someUIErrorOccurred(p0: String?) {
        Log.d("PaytmUIError", p0)
    }

    override fun onTransactionCancel(p0: String?, p1: Bundle?) {
        Log.d("PaytmTransCanceled", "$p0 , $p1")
    }

    override fun networkNotAvailable() {
        Log.d("PaytmNoNetwork", "No Network")
    }

    override fun onErrorLoadingWebPage(p0: Int, p1: String?, p2: String?) {
        Log.d("PaytmLoadingError", "$p0, $p1, $p2")
    }

    override fun onBackPressedCancelTransaction() {
        Log.d("PaytmBackCancel", "Back Pressed")
    }
}