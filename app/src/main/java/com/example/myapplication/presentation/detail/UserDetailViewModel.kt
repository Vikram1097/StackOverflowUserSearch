package com.example.myapplication.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.GetUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: Int = checkNotNull(savedStateHandle["userId"]) {
        "userId nav argument is required for UserDetailScreen"
    }

    private val _uiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val uiState: StateFlow<UserDetailUiState> = _uiState.asStateFlow()

    init {
        // Fetch user as soon as the ViewModel is created
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            getUserByIdUseCase(userId)
                .catch { throwable ->
                    _uiState.value = UserDetailUiState.Error(
                        throwable.message ?: "Failed to load user"
                    )
                }
                .collect { user ->
                    _uiState.value = UserDetailUiState.Success(user)
                }
        }
    }
}