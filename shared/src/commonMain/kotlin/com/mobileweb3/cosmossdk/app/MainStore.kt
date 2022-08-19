package com.mobileweb3.cosmossdk.app

import com.mobileweb3.cosmossdk.crypto.Address
import com.mobileweb3.cosmossdk.crypto.CosmosNetwork
import com.mobileweb3.cosmossdk.crypto.Entropy
import com.mobileweb3.cosmossdk.crypto.Mnemonic
import com.mobileweb3.cosmossdk.crypto.Utils
import com.mobileweb3.cosmossdk.crypto.networks
import io.github.aakira.napier.Napier
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MainState(
    val address: String,
    val mnemonic: List<String>,
    val selectedNetwork: CosmosNetwork
) : State

sealed class MainAction : Action {
    object GenerateNew : MainAction()
    class SelectNetwork(val cosmosNetwork: CosmosNetwork) : MainAction()
}

sealed class MainSideEffect : Effect {
    data class Message(val text: String) : MainSideEffect()
}

class MainStore : Store<MainState, MainAction, MainSideEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(MainState("", emptyList(), CosmosNetwork.CosmosMain))
    private val sideEffect = MutableSharedFlow<MainSideEffect>()

    init {
        createAddress()
    }

    override fun observeState(): StateFlow<MainState> = state

    override fun observeSideEffect(): Flow<MainSideEffect> = sideEffect

    override fun dispatch(action: MainAction) {
        Napier.d(tag = "MainStore", message = "Action: $action")

        when (action) {
            MainAction.GenerateNew -> {
                createAddress()
            }
            is MainAction.SelectNetwork -> {
                state.value = state.value.copy(
                    selectedNetwork = action.cosmosNetwork
                )
                createAddress()
            }
        }
    }

    private fun createAddress() {
        val entropy = Entropy.getEntropy()
        val mnemonic = Mnemonic.getRandomMnemonic(entropy)
        val createdAddress = Address.createAddressFromEntropyByNetwork(
            network = state.value.selectedNetwork,
            entropy = Utils.byteArrayToHexString(entropy),
            path = 0,
            customPath = 0
        )

        state.value = state.value.copy(
            address = createdAddress,
            mnemonic = mnemonic
        )
    }
}