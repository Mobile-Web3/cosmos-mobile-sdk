package com.mobileweb3.cosmossdk.crypto

fun ByteArray.toHexString() : String {
    return this.joinToString("") {
        java.lang.String.format("%02x", it)
    }
}