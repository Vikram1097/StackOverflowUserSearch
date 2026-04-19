package com.example.myapplication.domain.model


data class User(
    val userId: Int,
    val displayName: String,
    val reputation: Int,
    val profileImage: String,
    val location: String?,       // optional — not all users set a location
    val creationDate: Long,
    val link: String             // profile URL on Stack Overflow
)