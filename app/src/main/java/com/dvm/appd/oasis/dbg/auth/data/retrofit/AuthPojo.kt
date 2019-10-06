package com.dvm.appd.oasis.dbg.auth.data.retrofit

import com.google.gson.annotations.SerializedName

data class AuthPojo(

    @SerializedName("JWT")
    val jwt: String,
    val name: String,
    @SerializedName("user_id")
    val userId: String,
    val email: String,
    val phone: String,
    @SerializedName("qr_code")
    val qrCode: String

)