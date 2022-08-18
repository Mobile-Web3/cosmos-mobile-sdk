package com.mobileweb3.cosmossdk.android.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mobileweb3.cosmossdk.android.ui.AppTheme
import com.mobileweb3.cosmossdk.app.MainStore

@Preview
@Composable
private fun MainScreenPreview() {
    AppTheme {
        MainScreen(MainStore())
    }
}