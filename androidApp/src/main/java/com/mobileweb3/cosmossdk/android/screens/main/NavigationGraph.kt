package com.mobileweb3.cosmossdk.android.screens.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobileweb3.cosmossdk.android.screens.account.AccountScreen
import com.mobileweb3.cosmossdk.android.screens.create.CreateWalletScreen
import com.mobileweb3.cosmossdk.android.screens.delegate.DelegateScreen
import com.mobileweb3.cosmossdk.android.screens.restore.RestoreWithMnemonicScreen
import com.mobileweb3.cosmossdk.android.screens.restore.RestoreWithPrivateScreen
import com.mobileweb3.cosmossdk.app.MainStore

@Composable
fun NavigationGraph(
    navController: NavHostController,
    store: MainStore
) {
    NavHost(navController, startDestination = BottomNavItem.Create.route) {
        composable(BottomNavItem.Create.route) {
            CreateWalletScreen(store)
        }
        composable(BottomNavItem.RestoreWithMnemonic.route) {
            RestoreWithMnemonicScreen()
        }
        composable(BottomNavItem.RestoreWithPrivate.route) {
            RestoreWithPrivateScreen()
        }
        composable(BottomNavItem.Accounts.route) {
            AccountScreen()
        }
        composable(BottomNavItem.Delegate.route) {
            DelegateScreen()
        }
    }
}