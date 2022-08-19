package com.mobileweb3.cosmossdk.shared

import java.lang.System

actual object System {

    actual fun getCurrentMillis(): Long {
        return System.currentTimeMillis()
    }
}