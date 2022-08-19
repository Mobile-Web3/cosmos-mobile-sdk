package com.mobileweb3.cosmossdk.android.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmossdk.app.MainStore

@Composable
fun AccountScreen(
    store: MainStore
) {
    val state = store.observeState().collectAsState()

    Column(
        modifier = Modifier.padding(vertical = 80.dp)
    ) {
        state.value.accountsOfSelectedNetwork.forEach {
            Text(
                text = "${it.address}"
            )
        }
    }
}