package com.mobileweb3.cosmossdk.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class SomethingResponse(
    val data: List<Something>
)