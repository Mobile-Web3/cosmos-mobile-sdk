package com.mobileweb3.cosmossdk.crypto

expect object CryptoHelper {

    fun encrypt(
        alias: String,
        resource: String,
        withAuth: Boolean
    ): EncryptResult

    fun EncryptResult.getEncData(): String

    fun EncryptResult.getIvData(): String
}