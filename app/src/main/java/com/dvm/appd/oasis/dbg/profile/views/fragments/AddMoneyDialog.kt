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
import com.dvm.appd.oasis.dbg.profile.viewmodel.AddMoneyViewModel
import com.dvm.appd.oasis.dbg.profile.viewmodel.AddMoneyViewModelFactory
import kotlinx.android.synthetic.main.dia_wallet_add_money.*
import kotlinx.android.synthetic.main.dia_wallet_add_money.view.*
import java.lang.Exception

class AddMoneyDialog : DialogFragment() {
    private lateinit var addMoneyViewModel: AddMoneyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.dia_wallet_add_money, container, false)
        addMoneyViewModel = ViewModelProviders.of(this, AddMoneyViewModelFactory())[AddMoneyViewModel::class.java]
        rootView.addBtn.isClickable = true
        rootView.addBtn.setOnClickListener {
            if (rootView.amount.text.toString().isBlank())
                Toast.makeText(context!!, "Please fill amount", Toast.LENGTH_SHORT).show()
            else if (try { rootView.amount.text.toString().toInt() } catch (e: Exception) { 100000 } > 10000) {
                Toast.makeText(context!!, "You can add max 10,000 at a time", Toast.LENGTH_SHORT)
                    .show()
                rootView.amount.text.clear()
            } else {
                addMoneyViewModel.addMoney(rootView.amount.text.toString().toInt())
                loadingPBR.visibility = View.VISIBLE
                it.isClickable = false
            }
        }

        addMoneyViewModel.result.observe(this, Observer {
            when (it!!) {
                TransactionResult.Success -> {
                    loadingPBR.visibility = View.GONE
                    dismiss()
                    Toast.makeText(context!!, "Money added successfully!", Toast.LENGTH_SHORT).show()
                }
                is TransactionResult.Failure -> {
                    loadingPBR.visibility = View.GONE
                    Toast.makeText(context!!, (it as TransactionResult.Failure).message, Toast.LENGTH_SHORT).show()
                    rootView.addBtn.isClickable = true
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