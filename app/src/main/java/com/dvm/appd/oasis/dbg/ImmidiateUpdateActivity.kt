package com.dvm.appd.oasis.dbg

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_immidiate_update.*

class ImmidiateUpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_immidiate_update)

        bttn_update.setOnClickListener {
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
