package com.example.myapplication.data.remote.api

import com.example.myapplication.data.remote.dto.UserResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface StackExchangeApi {


    @GET("2.3/users")
    suspend fun searchUsers(
        @Query("inname") inname: String,
        @Query("site") site: String = "stackoverflow",
        @Query("pagesize") pageSize: Int = 20,
        @Query("order") order: String = "asc",
        @Query("sort") sort: String = "name"
    ): UserResponseDto


    @GET("2.3/users/{ids}")
    suspend fun getUserById(
        @Path("ids") ids: Int,
        @Query("site") site: String = "stackoverflow"
    ): UserResponseDto
}