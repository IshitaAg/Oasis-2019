package com.dvm.appd.oasis.dbg.shared.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Reduces the code we need to type for casting LiveData to MutableLiveData.
 * */
fun <T> LiveData<T>.asMut(): MutableLiveData<T> {
    return (this as? MutableLiveData<T>) ?: throw IllegalArgumentException("Not a MutableLiveData")
}