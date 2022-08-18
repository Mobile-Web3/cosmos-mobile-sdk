package com.mobileweb3.cosmossdk.crypto

import com.mobileweb3.cosmossdk.BuildConfig
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.crypto.MnemonicException

actual object Mnemonic {

    actual fun getRandomMnemonic(entropy: ByteArray): List<String> {
        return try {
            MnemonicCode.INSTANCE.toMnemonic(entropy)
        } catch (e: MnemonicException.MnemonicLengthException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            emptyList()
        }
    }

    actual fun toEntropy(mnemonic: List<String>): ByteArray? {
        return try {
            MnemonicCode().toEntropy(mnemonic)
        } catch (e: Exception) {
            null
        }
    }
}