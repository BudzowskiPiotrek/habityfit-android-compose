package com.piodev.habityfit.features.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.piodev.habityfit.R
import com.piodev.habityfit.core.model.NavOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()
    val menuOptions = listOf(
        NavOption(
            title = stringResource(R.string.app_nav_menu_routine),
            Icons.Default.FitnessCenter,
            route = "routines",
            enabled = true
        ),
        NavOption(
            title = stringResource(R.string.app_nav_menu_exercises),
            Icons.Default.MenuBook,
            route = "exercises",
            enabled = true
        ),
        NavOption(
            title = stringResource(R.string.app_nav_menu_workout),
            Icons.Default.TrendingUp,
            route = "workouts",
            enabled = true
        ),
        NavOption(
            title = stringResource(R.string.app_nav_menu_feed),
            Icons.Default.Public,
            route = "feed", enabled = true
        ),
        NavOption(
            title = stringResource(R.string.app_nav_menu_profile),
            Icons.Default.AccountCircle,
            route = "profile",
            enabled = true
        ),
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                menuOptions.forEach { section ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = section.icon,
                                contentDescription = section.title
                            )
                        },
                        label = { Text(section.title) },
                        selected = isSectionSelected(currentRoute, section.route),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.outlineVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        onClick = {
                            if (currentRoute != section.route) {
                                navController.navigate(section.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        AppMenuScreen(
            navController = navController,
            onLogout = onLogout,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

private fun isSectionSelected(currentRoute: String?, sectionRoute: String): Boolean {
    if (currentRoute == null) return false
    if (currentRoute == sectionRoute) return true
    return when (sectionRoute) {
        "routines" -> currentRoute.startsWith("routine_details") ||
                currentRoute.startsWith("edit_routine") ||
                currentRoute.startsWith("edit_exercise") ||
                currentRoute.startsWith("graphics") ||
                currentRoute == "add_routine"

        "workouts" -> currentRoute.startsWith("session_details") ||
                currentRoute.startsWith("edit_session") ||
                currentRoute.startsWith("set_edit") ||
                currentRoute == "add_workout"

        else -> false
    }
}