package com.mobileweb3.cosmossdk.crypto

val networks = listOf(
    CosmosNetwork.CosmosMain,
    CosmosNetwork.Osmosis
)

sealed class CosmosNetwork(
    val displayName: String,
    val rawNetworkName: String,
    val toBytesName: String,
    val mintScanReferencePart: String,
    val childNetworkNumber: Int,
    val HDDerivationPath: String
) {

    fun getAccountLink(address: String): String {
        return "https://mintscan.io/$toBytesName/account/$address}"
    }

    object CosmosMain : CosmosNetwork(
        displayName = "Cosmos",
        rawNetworkName = "cosmoshub-mainnet",
        toBytesName = "cosmos",
        mintScanReferencePart = "cosmos",
        childNetworkNumber = 118,
        HDDerivationPath = "m/44'/118'/0'/0/"
    )

    object Osmosis : CosmosNetwork(
        displayName = "Osmosis",
        rawNetworkName = "osmosis-mainnet",
        toBytesName = "osmo",
        mintScanReferencePart = "osmosis",
        childNetworkNumber = 118,
        HDDerivationPath = "m/44'/118'/0'/0/"
    )
}