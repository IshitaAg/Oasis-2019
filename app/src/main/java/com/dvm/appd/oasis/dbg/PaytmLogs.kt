package com.dvm.appd.oasis.dbg

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import com.dvm.appd.oasis.dbg.di.AppModule
import com.dvm.appd.oasis.dbg.di.wallet.WalletModule
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_paytm_logs.*
import java.lang.StringBuilder

class PaytmLogs : AppCompatActivity() {

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paytm_logs)

        recycler_payTm_logs.adapter = PaytmLogsAdapter()

        AppModule(application).providesAppDatabase(application).walletDao().getPayTmTransactions().subscribeOn(Schedulers.io()).subscribe({
            (recycler_payTm_logs.adapter as PaytmLogsAdapter).list = it
            (recycler_payTm_logs.adapter as PaytmLogsAdapter).notifyDataSetChanged()
        },{
            Toast.makeText(this, "An Error Occoured", Toast.LENGTH_LONG).show()
        })
    }
}
