package com.example.interview.source.local.mapping

import androidx.browser.trusted.Token
import com.example.interview.models.entities.LoginEntity
import com.example.interview.models.responses.get.LoginResponse

fun LoginEntity.toLoginResponse(): LoginResponse {
    return LoginResponse(
        token = this.token,
        refreshToken = "",
        expiration = ""
    )
}


fun LoginResponse.toLoginEntity(username:String,encryptedToken:String): LoginEntity {
    return LoginEntity(
        username = username,
        token = encryptedToken
    )
}
