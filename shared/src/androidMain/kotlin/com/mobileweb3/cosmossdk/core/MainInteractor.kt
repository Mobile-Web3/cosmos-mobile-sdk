package com.mobileweb3.cosmossdk.core

import android.content.Context
import com.mobileweb3.cosmossdk.core.datasource.network.SomethingLoader
import com.mobileweb3.cosmossdk.core.datasource.storage.AccountsStorage
import com.mobileweb3.cosmossdk.interactor.MainInteractor
import com.russhwolf.settings.AndroidSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json

fun MainInteractor.Companion.create(ctx: Context, withLog: Boolean) = MainInteractor(
    SomethingLoader(
        AndroidHttpClient(withLog)
    ),
    AccountsStorage(
        AndroidSettings(ctx.getSharedPreferences("accounts", Context.MODE_PRIVATE)),
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    )
).also {
    if (withLog) Napier.base(DebugAntilog())
}