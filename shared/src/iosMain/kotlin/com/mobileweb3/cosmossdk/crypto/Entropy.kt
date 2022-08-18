package com.mobileweb3.cosmossdk.crypto

actual object Entropy {

    actual fun getEntropy(): ByteArray {
        val seed = ByteArray(32)
        //SecureRandom().nextBytes(seed)
        return seed
    }
}