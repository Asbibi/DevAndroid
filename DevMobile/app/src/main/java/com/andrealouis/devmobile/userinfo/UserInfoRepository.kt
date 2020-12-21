package com.andrealouis.devmobile.userinfo

import com.andrealouis.devmobile.network.Api

class UserInfoRepository {

    suspend fun refresh(): UserInfo? {
        val userInfo = Api.userService.getInfo()
        return if (userInfo.isSuccessful) userInfo.body() else null
    }

    suspend fun update(userInfo: UserInfo) : UserInfo? {
        val editedUserInfo = Api.userService.update(userInfo)
        return if (editedUserInfo.isSuccessful) editedUserInfo.body() else null
    }

}