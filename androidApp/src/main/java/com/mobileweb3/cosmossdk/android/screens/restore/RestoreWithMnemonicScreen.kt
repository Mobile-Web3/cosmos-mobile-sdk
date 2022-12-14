package com.mobileweb3.cosmossdk.android.screens.restore

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmossdk.app.MainAction
import com.mobileweb3.cosmossdk.app.MainStore

@Composable
fun RestoreWithMnemonicScreen(
    store: MainStore
) {
    val text = remember { mutableStateOf("") }
    val state = store.observeState().collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 56.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Column {
                OutlinedTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    label = { Text(text = "Insert Mnemonic") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                state.value.addressesFromMnemonic.forEach {
                    Text(
                        text = it,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .wrapContentHeight(),
                    )
                }
            }
        }

        Button(
            onClick = {
                store.dispatch(MainAction.RestoreWithMnemonic(text.value))
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            content = {
                Text(text = "Restore")
            }
        )
    }
}