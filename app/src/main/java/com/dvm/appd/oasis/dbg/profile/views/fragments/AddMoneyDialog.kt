package com.dvm.appd.oasis.dbg.profile.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.profile.viewmodel.AddMoneyViewModel
import com.dvm.appd.oasis.dbg.profile.viewmodel.AddMoneyViewModelFactory
import kotlinx.android.synthetic.main.dia_wallet_add_money.*
import kotlinx.android.synthetic.main.dia_wallet_add_money.view.*
import java.lang.Exception

class AddMoneyDialog : DialogFragment() {
    private lateinit var addMoneyViewModel: AddMoneyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addMoneyViewModel = ViewModelProviders.of(this, AddMoneyViewModelFactory())[AddMoneyViewModel::class.java]
        val rootView = inflater.inflate(R.layout.dia_wallet_add_money, container, false)
        addMoneyViewModel.user.observe(this, Observer {
            when(it){
                true-> {
                    addPaytm.visibility = View.GONE
                    addPaytm.isClickable = false
                }
                false -> {
                   addBtn.visibility = View.GONE
                    addBtn.isClickable = false
                }
            }
        })
        rootView.addBtn.isClickable = true
        rootView.addBtn.setOnClickListener {
            if (rootView.amount.text.toString().isBlank())
            { if(context!=null)
                Toast.makeText(context!!, "Please fill amount", Toast.LENGTH_SHORT).show()}
            else if (try { rootView.amount.text.toString().toInt() } catch (e: Exception) { 100000 } > 10000) {
                if(context!=null)
                Toast.makeText(context!!, "You can add max 10,000 at a time", Toast.LENGTH_SHORT)
                    .show()
                rootView.amount.text.clear()
            } else if (try{ rootView.amount.text.toString().toInt() } catch (e: Exception) { 10000000 } < 0) {
                if(context!=null)
                Toast.makeText(context!!, "Please enter a positive amount", Toast.LENGTH_SHORT).show()
                rootView.amount.text.clear()
            } else {
                val a = activity!!
                a.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                rootView.addBtn.isClickable = false
                rootView.loadingPBR.visibility = View.VISIBLE
                addMoneyViewModel.addMoney(rootView.amount.text.toString().toInt())


            }
        }

        rootView.addPaytm.setOnClickListener {
            if(!addMoneyViewModel.authRepository.sharedPreferences.getBoolean(AuthRepository.Keys.payTmDisclaimerShown, false)) {
                AlertDialog.Builder(context).setTitle("Disclaimer").setMessage("Please note that the amount add via Paytm to the wallet is non-refundable and non-transferable").setNegativeButton("OK") {dialog, which ->
                    addMoneyViewModel.authRepository.sharedPreferences.edit().putBoolean(AuthRepository.Keys.payTmDisclaimerShown, true).apply()
                    dialog.dismiss()
                }.show()
            }
            if(rootView.amount.text.toString().isBlank()){
                if(context!=null)
                Toast.makeText(context!!, "Please fill amount", Toast.LENGTH_SHORT).show()
            }
            else if(try{ rootView.amount.text.toString().toInt() } catch (e: Exception) { 10000000 } > 10000) {
                if(context!=null)
                Toast.makeText(context!!, "You can add max 10,000 at a time", Toast.LENGTH_SHORT).show()
                rootView.amount.text.clear()
            } else if (try{ rootView.amount.text.toString().toInt() } catch (e: Exception) { 10000000 } < 0) {
                if(context!=null)
                Toast.makeText(context!!, "Please enter a positive amount", Toast.LENGTH_SHORT).show()
                rootView.amount.text.clear()
            } else {
                addMoneyViewModel.getCheckSum(this.parentFragment as ProfileFragment, rootView.amount.text.toString())
                dialog!!.dismiss()
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}