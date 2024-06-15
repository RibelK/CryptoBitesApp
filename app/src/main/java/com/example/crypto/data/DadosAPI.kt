package com.example.crypto.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST

interface CriptoBitesApi {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginUserRequestBody): UserInformation

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterUserRequestBody): UserInformation
}

@JsonClass(generateAdapter = true)
data class LoginUserRequestBody(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class RegisterUserRequestBody(
    @Json(name = "full_name") val fullName: String,
    @Json(name = "phone_number") val phoneNumber: String,
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class UserInformation(
    @Json(name = "full_name") val fullName: String,
    val email: String
)