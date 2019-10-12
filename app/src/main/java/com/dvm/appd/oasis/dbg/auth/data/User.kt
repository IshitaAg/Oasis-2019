package com.dvm.appd.oasis.dbg.auth.data

data class User(
    val jwt: String,
    val name: String,
    val userId: String,
    val email: String,
    val phone: String,
    val qrCode: String,
    val isBitsian:Boolean,
    val firstLogin:Boolean,
    val voted:Boolean
)