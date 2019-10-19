package com.dvm.appd.oasis.dbg.profile.views.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.profile.viewmodel.SendMoneyViewModel
import com.dvm.appd.oasis.dbg.profile.viewmodel.SendMoneyViewModelFactory
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.dia_wallet_send_money.view.*
import rx.android.schedulers.AndroidSchedulers
import java.lang.Exception
import java.util.concurrent.TimeUnit


class SendMoneyDialog : DialogFragment() {

    private lateinit var sendMoneyViewModel: SendMoneyViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.dia_wallet_send_money, container, false)
        sendMoneyViewModel =
            ViewModelProviders.of(this, SendMoneyViewModelFactory())[SendMoneyViewModel::class.java]
        rootView.SendBtn.isClickable = true

        RxView.clicks(rootView.SendBtn).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (rootView.Amount.text.toString().isBlank() || rootView.userId.text.toString().isBlank()) {
                if(context!=null)
                    Toast.makeText(context!!, "Please fill all the required fields!", Toast.LENGTH_SHORT).show()
            } else {
                rootView.loadingPBR.visibility = View.VISIBLE
                rootView.SendBtn.isClickable =false
                try {
                    val userId = rootView.userId.text.toString().toInt()
                    val amount = rootView.Amount.text.toString().toInt()
                    sendMoneyViewModel.transferMoney(userId, amount)
                } catch (e: Exception) {
                    Toast.makeText(context, "Please enter correct values", Toast.LENGTH_LONG).show()
                }


            }
        }
        sendMoneyViewModel.result.observe(this, Observer {
            when(it!!){
                TransactionResult.Success -> {
                    rootView.loadingPBR.visibility = View.GONE
                    dismiss()
                    Toast.makeText(context!!, "Money transferred successfully!", Toast.LENGTH_SHORT).show()
                }
                is TransactionResult.Failure -> {
                    rootView.loadingPBR.visibility = View.GONE
                    Toast.makeText(context!!, (it as TransactionResult.Failure).message, Toast.LENGTH_SHORT).show()
                    rootView.SendBtn.isClickable = true
                }
            }
        })
        return rootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}