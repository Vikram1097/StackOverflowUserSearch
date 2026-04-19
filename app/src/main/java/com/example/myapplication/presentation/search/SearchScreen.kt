package com.example.myapplication.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onUserClick: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    // Collect StateFlow as Compose State; lifecycle-aware so it stops collecting when paused
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("StackOverflow Users") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // --- Search row: TextField + Button ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = viewModel.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Search by name") },
                    singleLine = true,
                    // Trigger search when user taps "Search" on keyboard
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { viewModel.searchUsers() })
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = viewModel::searchUsers) {
                    Text("SEARCH")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Content area: reacts to the sealed-class UI state ---
            when (val state = uiState) {
                is SearchUiState.Idle -> {
                    // Nothing searched yet — show a hint
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Enter a name and tap SEARCH",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is SearchUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is SearchUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                is SearchUiState.Success -> {
                    if (state.users.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No users found for \"${viewModel.searchQuery}\"")
                        }
                    } else {
                        // List of up to 20 users sorted alphabetically
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(items = state.users, key = { it.userId }) { user ->
                                UserListItem(user = user, onClick = { onUserClick(user.userId) })
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun UserListItem(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reputation badge
            Text(
                text = user.reputation.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.widthIn(min = 60.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Display name
            Text(
                text = user.displayName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}