package com.mobileweb3.cosmossdk.crypto

expect object Address {

    fun createAddressFromEntropyByNetwork(
        network: CosmosNetwork,
        entropy: String,
        path: Int,
        customPath: Int
    ): String

    fun getDpAddress(
        network: CosmosNetwork,
        pubHex: String
    ): String
}