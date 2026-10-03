package com.piodev.habityfit.features.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.piodev.habityfit.R
import com.piodev.habityfit.core.model.NavOption
import com.piodev.habityfit.features.account.components.ProfileGridItem
import com.piodev.habityfit.features.account.viewmodel.AccountMenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onNavigateTo: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: AccountMenuViewModel = hiltViewModel()
) {
    val hasPendingRoutineRequests by viewModel.hasPendingRoutineRequests.collectAsStateWithLifecycle()
    val hasPendingFriendRequests by viewModel.hasPendingFriendRequests.collectAsStateWithLifecycle()

    val menuOptions = listOf(
        NavOption(
            title = stringResource(R.string.account_nav_menu_rm1),
            Icons.Default.BarChart,
            route = "rm",
            enabled = true
        ),
        NavOption(
            title = stringResource(R.string.account_nav_menu_info),
            Icons.Default.Description,
            route = "info",
            enabled = true
        ),
        NavOption(
            title = stringResource(R.string.account_nav_menu_composition),
            Icons.Default.MonitorHeart,
            route = "measurements",
            enabled = true
        ),
        NavOption(
            title = stringResource(R.string.account_nav_menu_community),
            Icons.Default.Diversity3,
            route = "community",
            enabled = true
        ),
        NavOption(
            title = stringResource(R.string.account_nav_menu_market),
            Icons.Default.Store,
            route = "store",
            enabled = false
        ),
        NavOption(
            title = stringResource(R.string.account_nav_menu_requests),
            Icons.Default.FitnessCenter,
            route = "routine_requests",
            enabled = true
        ),
        NavOption(
            title = stringResource(R.string.account_nav_menu_settings),
            Icons.Default.Settings,
            route = "settings",
            enabled = true
        ),
        NavOption(
            title = stringResource(R.string.account_nav_menu_logout),
            Icons.AutoMirrored.Filled.Logout,
            route = "logout",
            enabled = true
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(60.dp),
                windowInsets = WindowInsets(0, 0, 0, 0),
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.account_menu_title_text),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.sync() },
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.account_menu_synchronize_cd)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(menuOptions) { option ->
                    ProfileGridItem(
                        option = option,
                        showBadge = when (option.route) {
                            "routine_requests" -> hasPendingRoutineRequests
                            "community" -> hasPendingFriendRequests
                            else -> false
                        }
                    ) {
                        if (option.enabled) {
                            if (option.route == "logout") {
                                onLogout()
                            } else {
                                onNavigateTo(option.route)
                            }
                        }
                    }
                }
            }
        }
    }
}