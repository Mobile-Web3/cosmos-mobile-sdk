package com.mobileweb3.cosmossdk.app

import com.mobileweb3.cosmossdk.core.wrap

fun MainStore.watchState() = observeState().wrap()
fun MainStore.watchSideEffect() = observeSideEffect().wrap()