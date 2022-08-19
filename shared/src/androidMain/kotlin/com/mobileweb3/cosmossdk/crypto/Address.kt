package com.mobileweb3.cosmossdk.crypto

import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.crypto.DeterministicHierarchy
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.crypto.HDKeyDerivation
import org.bouncycastle.crypto.digests.RIPEMD160Digest
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import kotlin.experimental.and

actual object Address {

    private const val CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l"

    actual fun createAddressFromEntropyByNetwork(
        network: CosmosNetwork,
        entropy: String,
        path: Int,
        customPath: Int
    ): String {
        //        DeterministicKey childKey = getCreateKeyWithPathfromEntropy(chain, entropy, path, customPath);
        //        if (chain.equals(OKEX_MAIN) && customPath == 1 || chain.equals(OKEX_MAIN) && customPath == 2) {
        //            return generateEthAddressFromPrivateKey(childKey.getPrivateKeyAsHex());
        //        } else if (chain.equals(OKEX_MAIN) && customPath == 0) {
        //            return generateTenderAddressFromPrivateKey(childKey.getPrivateKeyAsHex());
        //        } else if (chain.equals(INJ_MAIN)) {
        //            return generateAddressFromPriv("inj", childKey.getPrivateKeyAsHex());
        //        } else if (chain.equals(EVMOS_MAIN)) {
        //            return generateAddressFromPriv("evmos", childKey.getPrivateKeyAsHex());
        //        }
        //        return getDpAddress(chain, childKey.getPublicKeyAsHex());

        val childKey = createKeyWithPathFromEntropy(network, entropy, path, customPath)
        return getDpAddress(network, childKey.publicKeyAsHex)
    }

    private fun createKeyWithPathFromEntropy(
        network: CosmosNetwork,
        entropy: String,
        path: Int,
        customPath: Int
    ): DeterministicKey {
        val masterKey = HDKeyDerivation.createMasterPrivateKey(Entropy.getHDSeed(entropy.decodeHex()))
        //cosmostation have separate func to get second childnumber and result path
        val parentPath = listOf(
            ChildNumber(44, true),
            ChildNumber(network.childNetworkNumber, true),
            ChildNumber.ZERO_HARDENED,
            ChildNumber.ZERO
        )
        return DeterministicHierarchy(masterKey).deriveChild(
            parentPath, true, true, ChildNumber(path)
        )
        //if network is FETCH_AI
        //        DeterministicKey masterKey = HDKeyDerivation.createMasterPrivateKey(getHDSeed(WUtil.HexStringToByteArray(entropy)));
        //        if (customPath != 2) {
        //            DeterministicKey targetKey = new DeterministicHierarchy(masterKey).deriveChild(WKey.getParentPath(chain, customPath), true, true, new ChildNumber(path));
        //            return targetKey;
        //        } else {
        //            DeterministicKey targetKey = new DeterministicHierarchy(masterKey).deriveChild(WKey.getParentPath(chain, customPath), true, true, new ChildNumber(path, true));
        //            DeterministicKey targetKey2 = new DeterministicHierarchy(targetKey).deriveChild(WKey.getFetchParentPath2(), true, true, ChildNumber.ZERO);
        //            return targetKey2;
        //        }
    }

    private fun getDpAddress(
        network: CosmosNetwork,
        pubHex: String
    ): String {
        val digest: MessageDigest = Sha256.sha256Digest
        val hash = digest.digest(pubHex.decodeHex())

        val digest2 = RIPEMD160Digest()
        digest2.update(hash, 0, hash.size)

        val hash3 = ByteArray(digest2.digestSize)
        digest2.doFinal(hash3, 0)

        val converted: ByteArray = convertBits(hash3, 8, 5, true)

        return bech32Encode(network.toBytesName.toByteArray(), converted)
    }

    @Throws(Exception::class)
    fun convertBits(data: ByteArray, frombits: Int, tobits: Int, pad: Boolean): ByteArray {
        var acc = 0
        var bits = 0
        val baos = ByteArrayOutputStream()
        val maxv = (1 shl tobits) - 1
        for (i in data.indices) {
            val value = data[i].toInt() and 0xff
            if (value ushr frombits != 0) {
                throw Exception("invalid data range: data[$i]=$value (frombits=$frombits)")
            }
            acc = acc shl frombits or value
            bits += frombits
            while (bits >= tobits) {
                bits -= tobits
                baos.write(acc ushr bits and maxv)
            }
        }
        if (pad) {
            if (bits > 0) {
                baos.write(acc shl tobits - bits and maxv)
            }
        } else if (bits >= frombits) {
            throw Exception("illegal zero padding")
        } else if (acc shl tobits - bits and maxv != 0) {
            throw Exception("non-zero padding")
        }
        return baos.toByteArray()
    }

    private fun bech32Encode(hrp: ByteArray, data: ByteArray): String {
        val chk: ByteArray = createChecksum(hrp, data)
        val combined = ByteArray(chk.size + data.size)

        System.arraycopy(data, 0, combined, 0, data.size)
        System.arraycopy(chk, 0, combined, data.size, chk.size)

        val xlat = ByteArray(combined.size)
        for (i in combined.indices) {
            xlat[i] = CHARSET[combined[i].toInt()].code.toByte()
        }
        val ret = ByteArray(hrp.size + xlat.size + 1)

        System.arraycopy(hrp, 0, ret, 0, hrp.size)
        System.arraycopy(byteArrayOf(0x31), 0, ret, hrp.size, 1)
        System.arraycopy(xlat, 0, ret, hrp.size + 1, xlat.size)

        return String(ret)
    }

    private fun createChecksum(hrp: ByteArray, data: ByteArray): ByteArray {
        val zeroes = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        val expanded: ByteArray = hrpExpand(hrp)
        val values = ByteArray(zeroes.size + expanded.size + data.size)

        System.arraycopy(expanded, 0, values, 0, expanded.size)
        System.arraycopy(data, 0, values, expanded.size, data.size)
        System.arraycopy(zeroes, 0, values, expanded.size + data.size, zeroes.size)

        val polymod: Int = polymod(values) xor 1

        val ret = ByteArray(6)
        for (i in ret.indices) {
            ret[i] = (polymod shr 5 * (5 - i) and 0x1f).toByte()
        }
        return ret
    }

    private fun hrpExpand(hrp: ByteArray): ByteArray {
        val buf1 = ByteArray(hrp.size)
        val buf2 = ByteArray(hrp.size)
        val mid = ByteArray(1)

        for (i in hrp.indices) {
            buf1[i] = (hrp[i].toInt() shr 5).toByte()
        }

        mid[0] = 0x00
        for (i in hrp.indices) {
            buf2[i] = hrp[i] and 0x1f
        }
        val ret = ByteArray(hrp.size * 2 + 1)

        System.arraycopy(buf1, 0, ret, 0, buf1.size)
        System.arraycopy(mid, 0, ret, buf1.size, mid.size)
        System.arraycopy(buf2, 0, ret, buf1.size + mid.size, buf2.size)

        return ret
    }

    private fun polymod(values: ByteArray): Int {
        val generators = intArrayOf(0x3b6a57b2, 0x26508e6d, 0x1ea119fa, 0x3d4233dd, 0x2a1462b3)
        var chk = 1
        for (b in values) {
            val top = (chk shr 0x19).toByte()
            chk = (b.toInt() xor (chk and 0x1ffffff shl 5))
            for (i in 0..4) {
                chk = chk xor if ((top.toInt() shr i and 1) == 1) generators[i] else 0
            }
        }
        return chk
    }
}