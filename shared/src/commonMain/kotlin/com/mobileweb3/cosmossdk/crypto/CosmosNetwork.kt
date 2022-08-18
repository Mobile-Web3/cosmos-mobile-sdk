package com.mobileweb3.cosmossdk.crypto

val networks = listOf(
    CosmosNetwork.CosmosMain,
    CosmosNetwork.Osmosis
)

sealed class CosmosNetwork(
    val displayName: String,
    val rawNetworkName: String,
    val toBytesName: String,
    val childNetworkNumber: Int
) {

    fun getAccountLink(address: String): String {
        return "https://mintscan.io/$toBytesName/account/$address}"
    }

    object CosmosMain : CosmosNetwork(
        displayName = "Cosmos",
        rawNetworkName = "cosmoshub-mainnet",
        toBytesName = "cosmos",
        childNetworkNumber = 118
    )

    object Osmosis : CosmosNetwork(
        displayName = "Osmosis",
        rawNetworkName = "osmosis-mainnet",
        toBytesName = "osmosis",
        childNetworkNumber = 118
    )
}