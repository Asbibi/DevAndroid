package com.andrealouis.devmobile.network

import com.andrealouis.devmobile.authentication.AuthenticationResponse
import com.andrealouis.devmobile.authentication.LoginForm
import com.andrealouis.devmobile.authentication.SignupForm
import com.andrealouis.devmobile.userinfo.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<AuthenticationResponse>

    @POST("users/sign_up")
    suspend fun signup(@Body user: SignupForm): Response<AuthenticationResponse>
}