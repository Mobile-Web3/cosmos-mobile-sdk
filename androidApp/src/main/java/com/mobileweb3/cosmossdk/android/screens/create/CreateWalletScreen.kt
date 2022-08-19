package com.mobileweb3.cosmossdk.android.screens.create

import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmossdk.app.MainAction
import com.mobileweb3.cosmossdk.app.MainStore

@Composable
fun CreateWalletScreen(
    store: MainStore
) {
    val uriHandler = LocalUriHandler.current
    val state = store.observeState().collectAsState()
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 56.dp)
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
                            uriHandler.openUri(state.value.selectedNetwork.getAccountLink(state.value.address))
                        },
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Generated mnemonic: ${state.value.mnemonic}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentHeight()
                        .clickable {
                            Toast
                                .makeText(context, "Mnemonic copied!", LENGTH_LONG)
                                .show()
                            clipboardManager.setText(AnnotatedString(state.value.mnemonic.joinToString(" ")))
                        },
                )
            }
        }

        Row {
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    store.dispatch(MainAction.GenerateNew)
                },
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(text = "Generate")
                }
            )

            Button(
                onClick = {
                    store.dispatch(MainAction.SaveAddressFromCreate)
                },
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(text = "Save")
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}