package com.dvm.appd.oasis.dbg

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_immidiate_update.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class ImmidiateUpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_immidiate_update)

        RxView.clicks(bttn_update).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://play.google.com/store/apps/details?id=v2015.oasis.pilani.bits.com.home")
                    // setPackage("v2015.oasis.pilani.bits.com.home")
                }
                startActivity(intent)
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=v2015.oasis.pilani.bits.com.home")))
            }
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
