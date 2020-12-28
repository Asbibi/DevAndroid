package com.andrealouis.devmobile.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
        @SerialName("token")
        val token: String
) : java.io.Serializable
