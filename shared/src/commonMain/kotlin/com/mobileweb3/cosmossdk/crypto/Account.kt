package com.mobileweb3.cosmossdk.crypto

import com.mobileweb3.cosmossdk.shared.UUID
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    var id: Long,
    val uuid: String,
    val nickName: String? = null,
    val isFavourite: String? = null,
    val address: String? = null,
    val network: String? = null,
    val hasPrivateKey: String? = null,
    val resource: String? = null,
    val spec: String? = null,
    val fromMnemonic: String? = null,
    val path: String? = null,
    val isValidator: Boolean? = null,
    val sequenceNumber: Int? = null,
    val accountNumber: Int? = null,
    val fetchTime: Long? = null,
    val mSize: Int? = null,
    val importTime: Long? = null,
    val lastTotal: String? = null,
    val sortOrder: Long? = null,
    val newBip44: Boolean? = null,
    val customPath: Int? = null
) {

    companion object {
        fun newInstance(id: Long): Account {
            return Account(
                id = id,
                uuid = UUID.generateUUID()
            )
        }
    }
}