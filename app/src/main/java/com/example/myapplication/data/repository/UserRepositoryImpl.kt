package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.api.StackExchangeApi
import com.example.myapplication.data.remote.dto.toDomain
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: StackExchangeApi
) : UserRepository {


    override fun searchUsers(name: String): Flow<List<User>> = flow {
        val response = api.searchUsers(inname = name)
        // Sort alphabetically in case the API order differs; cap at 20
        val users = response.items
            .map { it.toDomain() }
            .sortedBy { it.displayName.lowercase() }
            .take(20)
        emit(users)
    }


    override fun getUserById(userId: Int): Flow<User> = flow {
        val response = api.getUserById(ids = userId)
        val user = response.items.firstOrNull()?.toDomain()
            ?: throw NoSuchElementException("User $userId not found")
        emit(user)
    }
}