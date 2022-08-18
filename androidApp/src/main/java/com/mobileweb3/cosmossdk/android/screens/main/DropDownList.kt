package com.mobileweb3.cosmossdk.android.screens.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mobileweb3.cosmossdk.crypto.CosmosNetwork

@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<CosmosNetwork>,
    request: (Boolean) -> Unit,
    selectedIndex: (Int) -> Unit
) {
    DropdownMenu(
//        toggle = {
//            // Implement your toggle
//        },
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEachIndexed { index, network ->
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedIndex(index)
                }
            ) {
                Text(
                    network.displayName,
                    modifier = Modifier
                        .wrapContentWidth()
                )
            }
        }
    }
}