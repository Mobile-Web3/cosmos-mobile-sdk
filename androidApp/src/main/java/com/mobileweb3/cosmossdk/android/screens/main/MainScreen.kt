package com.mobileweb3.cosmossdk.android.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmossdk.app.MainAction
import com.mobileweb3.cosmossdk.app.MainStore

@Composable
fun MainScreen(
    store: MainStore
) {
    val uriHandler = LocalUriHandler.current
    val state = store.observeState().collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = "address: ${state.value.address}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentHeight()
                        .clickable {
                            uriHandler.openUri("https://mintscan.io/cosmos/account/${state.value.address}")
                        },
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Generated mnemonic: ${state.value.mnemonic}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentHeight(),
                )
            }
        }

        Button(
            onClick = {
                store.dispatch(MainAction.GenerateNew)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            content = {
                Text(text = "Generate")
            }
        )
    }
}