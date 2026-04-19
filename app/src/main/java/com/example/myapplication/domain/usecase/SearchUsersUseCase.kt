package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(name: String): Flow<List<User>> =
        repository.searchUsers(name)
}