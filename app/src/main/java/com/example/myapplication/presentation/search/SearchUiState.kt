package com.example.myapplication.presentation.search

import com.example.myapplication.domain.model.User


sealed class SearchUiState {

    data object Idle : SearchUiState()

    data object Loading : SearchUiState()

    data class Success(val users: List<User>) : SearchUiState()

    data class Error(val message: String) : SearchUiState()
}