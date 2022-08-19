package com.mobileweb3.cosmossdk.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobileweb3.cosmossdk.android.ui.DropdownNetworkSelection
import com.mobileweb3.cosmossdk.crypto.CosmosNetwork
import com.mobileweb3.cosmossdk.crypto.networks

@Composable
fun NetworkSelection(
    onNetworkSelected: (CosmosNetwork) -> Unit
) {
    val text = remember { mutableStateOf("Cosmos") } // initial value
    val isOpen = remember { mutableStateOf(false) } // initial value
    val openCloseOfDropDownList: (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelectedString: (Int) -> Unit = {
        text.value = networks[it].displayName
        onNetworkSelected.invoke(networks[it])
    }
    Box {
        Column {
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text(text = "Select network") },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownNetworkSelection(
                requestToOpen = isOpen.value,
                list = networks,
                openCloseOfDropDownList,
                userSelectedString
            )
        }
        Spacer(
            modifier = Modifier.matchParentSize().background(Color.Transparent).padding(10.dp)
                .clickable(
                    onClick = { isOpen.value = true }
                )
        )
    }
}