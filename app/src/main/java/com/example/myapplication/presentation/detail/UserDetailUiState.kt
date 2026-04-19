package com.example.myapplication.presentation.detail

import com.example.myapplication.domain.model.User


sealed class UserDetailUiState {

    data object Loading : UserDetailUiState()

    data class Success(val user: User) : UserDetailUiState()

    data class Error(val message: String) : UserDetailUiState()
}