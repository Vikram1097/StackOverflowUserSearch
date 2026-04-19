package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {


    fun searchUsers(name: String): Flow<List<User>>

    fun getUserById(userId: Int): Flow<User>
}