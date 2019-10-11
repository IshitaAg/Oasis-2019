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
import android.view.animation.Animation
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
import com.google.gson.JsonObject
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.labo.kaji.fragmentanimations.FlipAnimation
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
        //(activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()
        (activity!! as MainActivity).setStatusBarColor(R.color.status_bar_profile)

        rootView.logout.setOnClickListener {
            profileViewModel.logout()
        }

        profileViewModel.balance.observe(this, Observer {
            rootView.balance.text = context!!.resources.getString(R.string.rupee)+it!!
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


        profileViewModel.getCheckSum(stagingPgService, prodPgService, this, "2.00")

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

    override fun onTransactionResponse(bundle: Bundle?) {
        Log.d("PayTm", "on Transaction Response ${bundle.toString()}")
        if (!(bundle!!.isEmpty)) {
            if(bundle["STATUS"].toString() == "TXN_SUCCESS") {
                val body = JsonObject().apply {
                    this.addProperty("STATUS", bundle["STATUS"].toString())
                    this.addProperty("CHECKSUMHASH", bundle["CHECKSUMHASH"].toString())
                    this.addProperty("BANKNAME", bundle["BANKNAME"].toString())
                    this.addProperty("ORDERID", bundle["ORDERID"].toString())
                    this.addProperty("TXNAMOUNT", bundle["TXNAMOUNT"].toString())
                    this.addProperty("TXNDATE", bundle["TXNDATE"].toString())
                    this.addProperty("MID", bundle["MID"].toString())
                    this.addProperty("TXNID", bundle["TXNID"].toString())
                    this.addProperty("RESPCODE", bundle["RESPCODE"].toString())
                    this.addProperty("PAYMENTMODE", bundle["PAYMENTMODE"].toString())
                    this.addProperty("BANKTXNID", bundle["BANKTXNID"].toString())
                    this.addProperty("CURRENCY", bundle["CURRENCY"].toString())
                    this.addProperty("GATEWAYNAME", bundle["GATEWAYNAME"].toString())
                    this.addProperty("RESPMSG", bundle["RESPMSG"].toString())
                    Log.d("PayTm", "Sent request body for confirmation = ${this.toString()}")
                }
                profileViewModel.onPaytmTransactionSucessful(body)
            }
        }
    }

    override fun clientAuthenticationFailed(p0: String?) {
        Log.d("PayTm", "Client authentication failed ${p0}")
    }

    override fun someUIErrorOccurred(p0: String?) {
        Log.d("PayTm", "Some UI error occoured $p0")
    }

    override fun onTransactionCancel(p0: String?, p1: Bundle?) {
        Log.d("PayTm", "Transaction cancled $p0 \n $p1")
    }

    override fun networkNotAvailable() {
        Log.d("PayTm", "Network not available")
    }

    override fun onErrorLoadingWebPage(p0: Int, p1: String?, p2: String?) {
        Log.d("PayTm", "Error in loading the webpage $p0\n $p1\n $p2")
    }

    override fun onBackPressedCancelTransaction() {
        Log.d("PayTm", "Transaction was cancelled because of back pressed")
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return FlipAnimation.create(FlipAnimation.RIGHT, enter, 1000)
    }
}