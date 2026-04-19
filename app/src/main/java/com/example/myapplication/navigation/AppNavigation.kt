package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.presentation.detail.UserDetailScreen
import com.example.myapplication.presentation.search.SearchScreen

private object Routes {
    const val SEARCH = "search"
    const val USER_DETAIL = "user_detail/{userId}"

    fun userDetail(userId: Int) = "user_detail/$userId"
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH
    ) {
        // --- Search Screen ---
        composable(route = Routes.SEARCH) {
            SearchScreen(
                onUserClick = { userId ->
                    navController.navigate(Routes.userDetail(userId))
                }
            )
        }

        // --- User Detail Screen ---
        composable(
            route = Routes.USER_DETAIL,
            arguments = listOf(
                // Declare the userId argument type so Navigation can restore it from the back-stack
                navArgument("userId") { type = NavType.IntType }
            )
        ) {
            UserDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}