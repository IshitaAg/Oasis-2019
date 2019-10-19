package com.dvm.appd.oasis.dbg.auth.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dvm.appd.oasis.dbg.R
import com.dvm.appd.oasis.dbg.auth.viewmodel.PictureActivityViewModel
import com.dvm.appd.oasis.dbg.auth.viewmodel.PictureActivityViewModelFactory
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_picture.*
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class PictureActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    private val pictureActivityViewModel by lazy {
        ViewModelProviders.of(
            this,
            PictureActivityViewModelFactory()
        )[PictureActivityViewModel::class.java]
    }
    lateinit var picBt: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.CAMERA
                ), 101
            )
        }
        picBt = picBtn
        RxView.clicks(picBtn).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

        pictureActivityViewModel.state.observe(this, Observer {
            when(it){
                LoginState.MoveToOnBoarding -> {

                    startActivity(Intent(this, OnboardingActivity::class.java))
                    finish()
                }
                is LoginState.Failure -> {

                    Toast.makeText(this, (it as LoginState.Failure).message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView8.isDrawingCacheEnabled = true
            imageView8.buildDrawingCache()
            val imageBitmap = data!!.extras.get("data") as Bitmap
            imageView8.setImageBitmap(imageBitmap)
            picBt.isClickable = false
            Toast.makeText(this, "Uploading your image! Please wait...", Toast.LENGTH_SHORT).show()
            pictureActivityViewModel.uploadImage(imageBitmap)

        }
    }
}