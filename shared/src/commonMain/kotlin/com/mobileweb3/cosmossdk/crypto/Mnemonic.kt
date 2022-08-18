package com.mobileweb3.cosmossdk.crypto

expect object Mnemonic {

    fun getRandomMnemonic(entropy: ByteArray): List<String>

    fun toEntropy(mnemonic: List<String>): ByteArray?
}