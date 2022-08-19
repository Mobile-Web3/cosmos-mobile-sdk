package com.mobileweb3.cosmossdk.android.screens.main

sealed class BottomNavItem(val title: String, val route: String) {

    object Create : BottomNavItem("Create wallet", "create")
    object RestoreWithMnemonic : BottomNavItem("Restore with mnemonic", "restore_mnemonic")
    object RestoreWithPrivate : BottomNavItem("Restore with private", "restore_private")
    object Accounts : BottomNavItem("Accounts", "accounts")
    object Delegate : BottomNavItem("Delegate", "delegate")
}