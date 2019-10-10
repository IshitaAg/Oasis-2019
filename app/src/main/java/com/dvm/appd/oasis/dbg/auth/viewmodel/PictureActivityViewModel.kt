package com.dvm.appd.oasis.dbg.auth.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.auth.views.LoginState
import com.dvm.appd.oasis.dbg.shared.util.asMut
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single
import java.io.ByteArrayOutputStream

class PictureActivityViewModel(val authRepository: AuthRepository):ViewModel() {

    var state : LiveData<LoginState> = MutableLiveData()

    fun uploadImage(image:Bitmap){

        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val finalImage = baos.toByteArray()
        FirebaseStorage.getInstance().reference.child(authRepository.getUser().blockingGet().userId).putBytes(finalImage).addOnSuccessListener {

                state.asMut().postValue(LoginState.MoveToOnBoarding)


        }.addOnFailureListener {
            state.asMut().postValue(LoginState.Failure("Something went wrong!"))
            Log.d("check",it.toString())
        }

    }
}