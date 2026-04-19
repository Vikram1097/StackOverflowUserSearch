package com.example.myapplication.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    var searchQuery by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }


    fun searchUsers() {
        if (searchQuery.isBlank()) return

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading

            searchUsersUseCase(searchQuery)
                .catch { throwable ->
                    // Emit error state instead of crashing
                    _uiState.value = SearchUiState.Error(
                        throwable.message ?: "An unexpected error occurred"
                    )
                }
                .collect { users ->
                    _uiState.value = SearchUiState.Success(users)
                }
        }
    }
}