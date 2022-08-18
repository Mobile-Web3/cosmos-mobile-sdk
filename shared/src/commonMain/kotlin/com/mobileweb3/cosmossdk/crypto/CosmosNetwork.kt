package com.mobileweb3.cosmossdk.crypto

val networks = listOf(
    CosmosNetwork.CosmosMain(),

)

sealed class CosmosNetwork(
    val rawNetworkName: String,
    val toBytesName: String,
    val childNetworkNumber: Int
) {

    class CosmosMain : CosmosNetwork(
        rawNetworkName = "cosmoshub-mainnet",
        toBytesName = "cosmos",
        childNetworkNumber = 118
    )
}