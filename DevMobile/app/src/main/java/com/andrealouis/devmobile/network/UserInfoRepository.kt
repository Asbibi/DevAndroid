package com.andrealouis.devmobile.network

class UserInfoRepository {

    suspend fun refresh(): UserInfo? {
        val userInfo = Api.userService.getInfo()
        return if (userInfo.isSuccessful) userInfo.body() else null
    }

}