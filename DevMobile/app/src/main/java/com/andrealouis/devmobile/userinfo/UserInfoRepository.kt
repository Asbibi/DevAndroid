package com.andrealouis.devmobile.userinfo

import com.andrealouis.devmobile.network.Api

class UserInfoRepository {

    suspend fun refresh(): UserInfo? {
        val userInfo = Api.INSTANCE.USER_WEB_SERVICE.getInfo()
        return if (userInfo.isSuccessful) userInfo.body() else null
    }

    suspend fun update(userInfo: UserInfo) : UserInfo? {
        val editedUserInfo = Api.INSTANCE.USER_WEB_SERVICE.update(userInfo)
        return if (editedUserInfo.isSuccessful) editedUserInfo.body() else null
    }

}