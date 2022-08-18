package com.mobileweb3.cosmossdk.crypto

expect object Entropy {

    fun getEntropy(): ByteArray
    
    fun getHDSeed(entropy: ByteArray): ByteArray?
}