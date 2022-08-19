package com.mobileweb3.cosmossdk.android.screens.main

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.mobileweb3.cosmossdk.android.ui.NetworkSelection
import com.mobileweb3.cosmossdk.app.MainAction
import com.mobileweb3.cosmossdk.app.MainStore

@Composable
fun MainScreen(
    store: MainStore
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        NetworkSelection(
            onNetworkSelected = {
                store.dispatch(MainAction.SelectNetwork(it))
            }
        )

        NavigationGraph(navController = navController, store)
    }
}