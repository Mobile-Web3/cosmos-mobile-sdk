package com.mobileweb3.cosmossdk.interactor

import com.mobileweb3.cosmossdk.core.datasource.network.SomethingLoader
import com.mobileweb3.cosmossdk.core.datasource.storage.AccountsStorage
import com.mobileweb3.cosmossdk.crypto.Account
import com.mobileweb3.cosmossdk.crypto.CosmosNetwork

class MainInteractor internal constructor(
    private val somethingLoader: SomethingLoader,
    private val accountsStorage: AccountsStorage,
){

    @Throws(Exception::class)
    suspend fun getAllAccounts(network: CosmosNetwork): List<Account> {
        var accounts = accountsStorage.getAllAccounts()

        return accounts.filter { it.network == network.displayName }
    }

    @Throws(Exception::class)
    suspend fun saveAccount(account: Account) {
        accountsStorage.saveAccount(account)
    }

    @Throws(Exception::class)
    suspend fun deleteAccount(id: Long) {
        accountsStorage.deleteAccount(id)
    }

    fun getIdForNewAccount(): Long {
        return accountsStorage.nextAccountId
    }

    companion object
}