package com.example.myapplication.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    onBack: () -> Unit,
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UserDetailUiState.Loading -> CircularProgressIndicator()

                is UserDetailUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is UserDetailUiState.Success -> UserDetailContent(state)
            }
        }
    }
}

/**
 * Renders the user profile card once data is loaded.
 */
@Composable
private fun UserDetailContent(state: UserDetailUiState.Success) {
    val user = state.user

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Avatar ---
        AsyncImage(
            model = user.profileImage,
            contentDescription = "${user.displayName} avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)       // circular avatar shape
        )

        // --- Name ---
        Text(
            text = user.displayName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        HorizontalDivider()

        // --- Info rows ---
        UserInfoRow(
            icon = { Icon(Icons.Filled.Star, contentDescription = null) },
            label = "Reputation",
            value = "%,d".format(user.reputation)   // e.g. "123,456"
        )

        user.location?.takeIf { it.isNotBlank() }?.let { location ->
            UserInfoRow(
                icon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                label = "Location",
                value = location
            )
        }

        UserInfoRow(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = null) },
            label = "Member since",
            value = user.creationDate.toFormattedDate()
        )

        UserInfoRow(
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            label = "User ID",
            value = "#${user.userId}"
        )
    }
}

/**
 * A single labelled row with an icon, label, and value.
 */
@Composable
private fun UserInfoRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        icon()
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun Long.toFormattedDate(): String {
    val date = Date(this * 1_000L)   // API gives seconds, Java Date needs milliseconds
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}