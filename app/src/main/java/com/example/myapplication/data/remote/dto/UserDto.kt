package com.example.myapplication.data.remote.dto

import com.example.myapplication.domain.model.User
import com.google.gson.annotations.SerializedName


data class UserResponseDto(
    @SerializedName("items") val items: List<UserDto>,
    @SerializedName("has_more") val hasMore: Boolean = false
)


data class UserDto(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("reputation") val reputation: Int,
    @SerializedName("profile_image") val profileImage: String = "",
    @SerializedName("location") val location: String?,       // nullable — not all users set this
    @SerializedName("creation_date") val creationDate: Long,
    @SerializedName("link") val link: String = ""
)

fun UserDto.toDomain(): User = User(
    userId = userId,
    displayName = displayName,
    reputation = reputation,
    profileImage = profileImage,
    location = location,
    creationDate = creationDate,
    link = link
)