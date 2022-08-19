package com.mobileweb3.cosmossdk.android.screens.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.mobileweb3.cosmossdk.app.MainStore

@Composable
fun MainScreen(
    store: MainStore
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        NavigationGraph(navController = navController, store)
    }
}