package com.mobileweb3.cosmossdk.crypto

object Utils {

    fun byteArrayToHexString(bytes: ByteArray): String {
        val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (j in bytes.indices) {
            v = (bytes[j].toInt() and 0xFF)
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return hexChars.concatToString()
    }

    @Throws(IllegalArgumentException::class)
    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        require(len % 2 != 1) { "Hex string must have even number of characters" }
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((s[i].digitToIntOrNull(16) ?: (-1 shl 4)) + s[i + 1].digitToIntOrNull(16)!!).toByte()
            i += 2
        }
        return data
    }

}