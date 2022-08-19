package com.mobileweb3.cosmossdk.app

import com.mobileweb3.cosmossdk.crypto.Account
import com.mobileweb3.cosmossdk.crypto.Address
import com.mobileweb3.cosmossdk.crypto.CosmosNetwork
import com.mobileweb3.cosmossdk.crypto.CryptoHelper
import com.mobileweb3.cosmossdk.crypto.CryptoHelper.getEncData
import com.mobileweb3.cosmossdk.crypto.CryptoHelper.getIvData
import com.mobileweb3.cosmossdk.crypto.Entropy
import com.mobileweb3.cosmossdk.crypto.Mnemonic
import com.mobileweb3.cosmossdk.crypto.PrivateKey
import com.mobileweb3.cosmossdk.crypto.Utils
import com.mobileweb3.cosmossdk.interactor.MainInteractor
import com.mobileweb3.cosmossdk.shared.System
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MainState(
    val address: String,
    val mnemonic: List<String>,
    val selectedNetwork: CosmosNetwork,
    val addressFromPrivate: String = "",
    val addressesFromMnemonic: List<String> = emptyList(),
    val accountsOfSelectedNetwork: List<Account> = emptyList()
) : State

sealed class MainAction : Action {
    object GenerateNew : MainAction()
    object SaveAddressFromCreate : MainAction()
    class RestoreWithMnemonic(val enteredMnemonic: String) : MainAction()
    class RestoreWithPrivate(val enteredPrivateKey: String) : MainAction()
    class SelectNetwork(val cosmosNetwork: CosmosNetwork) : MainAction()
}

sealed class MainSideEffect : Effect {
    data class Message(val text: String) : MainSideEffect()
}

class MainStore(val interactor: MainInteractor) : Store<MainState, MainAction, MainSideEffect>, CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val state = MutableStateFlow(MainState("", emptyList(), CosmosNetwork.CosmosMain))
    private val sideEffect = MutableSharedFlow<MainSideEffect>()

    init {
        //createAddress()
        launch {
            state.value = state.value.copy(
                accountsOfSelectedNetwork = interactor.getAllAccounts(state.value.selectedNetwork)
            )
        }
    }

    override fun observeState(): StateFlow<MainState> = state

    override fun observeSideEffect(): Flow<MainSideEffect> = sideEffect

    override fun dispatch(action: MainAction) {
        Napier.d(tag = "MainStore", message = "Action: $action")

        when (action) {
            MainAction.GenerateNew -> {
                createAddress()
            }
            MainAction.SaveAddressFromCreate -> {
                saveAddress()
            }
            is MainAction.SelectNetwork -> {
                launch {
                    state.value = state.value.copy(
                        selectedNetwork = action.cosmosNetwork,
                        accountsOfSelectedNetwork = interactor.getAllAccounts(action.cosmosNetwork)
                    )
                }
            }
            is MainAction.RestoreWithPrivate -> {
                restorePubFromPriv(action.enteredPrivateKey)
            }
            is MainAction.RestoreWithMnemonic -> {
                restorePubFromMnemonic(action.enteredMnemonic)
            }
        }
    }

    private fun saveAddress() {
        val newAccount = Account.newInstance(id = interactor.getIdForNewAccount()).apply {
            val encryptResult = CryptoHelper.encrypt(
                alias = "MNEMONIC_KEY" + this.uuid,
                resource = Utils.byteArrayToHexString(Mnemonic.toEntropy(state.value.mnemonic)!!),
                withAuth = false
            )

            this.resource = encryptResult.getEncData()
            this.spec = encryptResult.getIvData()

            this.address = state.value.address
            this.network = state.value.selectedNetwork.displayName
            this.hasPrivateKey = true

            this.fromMnemonic = true
            //TODO check path from other networks
            this.path = 0
            this.mnemonicSize = 24
            this.importTime = System.getCurrentMillis()
            //TODO check customPath from other networks
            this.customPath = 0
        }

        launch {
            interactor.saveAccount(newAccount)

            val accounts = interactor.getAllAccounts(state.value.selectedNetwork)
            state.value = state.value.copy(
                accountsOfSelectedNetwork = accounts
            )
        }
    }

    private fun restorePubFromMnemonic(enteredMnemonic: String) {
        if (enteredMnemonic.isEmpty()) {
            state.value = state.value.copy(
                addressesFromMnemonic = listOf("Enter Mnemonic!")
            )
            return
        }

        val mnemonicWords = enteredMnemonic.split(" ")
        val mnemonicSize = mnemonicWords.size
        if (mnemonicSize != 12 && mnemonicSize != 16 && mnemonicSize != 24) {
            state.value = state.value.copy(
                addressesFromMnemonic = listOf("Wrong Mnemonic words count = ${mnemonicWords.size}")
            )
        } else {
            if (Mnemonic.isValidMnemonic(mnemonicWords) && Mnemonic.isValidStringHdSeedFromWords(mnemonicWords)) {
                val selectedNetwork = state.value.selectedNetwork
                val entropy = Utils.byteArrayToHexString(Mnemonic.toEntropy(mnemonicWords)!!)
                val address0 = Address.createAddressFromEntropyByNetwork(
                    network = selectedNetwork,
                    entropy = entropy,
                    path = 0,
                    customPath = 0
                )
                val address1 = Address.createAddressFromEntropyByNetwork(
                    network = selectedNetwork,
                    entropy = entropy,
                    path = 1,
                    customPath = 0
                )
                val address2 = Address.createAddressFromEntropyByNetwork(
                    network = selectedNetwork,
                    entropy = entropy,
                    path = 2,
                    customPath = 0
                )
                val address3 = Address.createAddressFromEntropyByNetwork(
                    network = selectedNetwork,
                    entropy = entropy,
                    path = 3,
                    customPath = 0
                )
                val address4 = Address.createAddressFromEntropyByNetwork(
                    network = selectedNetwork,
                    entropy = entropy,
                    path = 4,
                    customPath = 0
                )
                state.value = state.value.copy(
                    addressesFromMnemonic = listOf(
                        "${selectedNetwork.HDDerivationPath}0\n$address0\n",
                        "${selectedNetwork.HDDerivationPath}1\n$address1\n",
                        "${selectedNetwork.HDDerivationPath}2\n$address2\n",
                        "${selectedNetwork.HDDerivationPath}3\n$address3\n",
                        "${selectedNetwork.HDDerivationPath}4\n$address4"
                    )
                )
            } else {
                state.value = state.value.copy(
                    addressesFromMnemonic = listOf("Invalid Mnemonic")
                )
            }
        }
    }

    private fun restorePubFromPriv(enteredPrivateKey: String) {
        var userInput = enteredPrivateKey.trim()

        if (userInput.isEmpty()) {
            state.value = state.value.copy(
                addressFromPrivate = "Enter Private Key!"
            )
            return
        }

        if (userInput.lowercase().startsWith("0x")) {
            userInput = userInput.substring(2)
        }

        if (!PrivateKey.isValid(userInput)) {
            state.value = state.value.copy(
                addressFromPrivate = "Invalid Private Key"
            )
            return
        }

        val address = Address.getDpAddress(state.value.selectedNetwork, PrivateKey.generatePubHexFromPrivate(userInput))
        state.value = state.value.copy(
            addressFromPrivate = address
        )
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
            mnemonic = mnemonic,
            addressFromPrivate = ""
        )
    }
}