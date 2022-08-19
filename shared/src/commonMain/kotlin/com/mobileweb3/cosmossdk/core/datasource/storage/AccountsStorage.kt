package com.mobileweb3.cosmossdk.core.datasource.storage

import com.mobileweb3.cosmossdk.crypto.Account
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class AccountsStorage(
    private val settings: Settings,
    private val json: Json
) {

    val nextAccountId: Long
        get() {
            val idFromSettings = settings.getLong(KEY_ACCOUNT_ID_CACHE, 0)
            settings.putLong(KEY_ACCOUNT_ID_CACHE, idFromSettings + 1)
            return idFromSettings
        }
    private var diskCache: Map<Long, Account>
        get() {
            return settings.getStringOrNull(KEY_ACCOUNT_CACHE)?.let { str ->
                json.decodeFromString(ListSerializer(Account.serializer()), str).associateBy { it.id }
            } ?: mutableMapOf()
        }
        set(value) {
            val list = value.map { it.value }
            settings[KEY_ACCOUNT_CACHE] =
                json.encodeToString(ListSerializer(Account.serializer()), list)
        }

    private val memCache: MutableMap<Long, Account> by lazy { diskCache.toMutableMap() }

    suspend fun getAccount(id: Long): Account? = memCache[id]

    suspend fun saveAccount(account: Account) {
        memCache[account.id] = account
        diskCache = memCache
    }

    suspend fun saveAccounts(accounts: List<Account>) {
        accounts.forEach {
            memCache[it.id] = it
        }

        diskCache = memCache
    }

    suspend fun deleteAccount(id: Long) {
        memCache.remove(id)
        diskCache = memCache
    }

    suspend fun getAllAccounts(): List<Account> = memCache.values.toList()

    private companion object {
        private const val KEY_ACCOUNT_CACHE = "key_account_cache"
        private const val KEY_ACCOUNT_ID_CACHE = "key_account_id_cache"
    }
}