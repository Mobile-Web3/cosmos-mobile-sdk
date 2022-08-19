package com.mobileweb3.cosmossdk.crypto

expect object PrivateKey {

    fun isValid(privateKey: String): Boolean

    fun generatePubHexFromPrivate(privateKey: String): String
}