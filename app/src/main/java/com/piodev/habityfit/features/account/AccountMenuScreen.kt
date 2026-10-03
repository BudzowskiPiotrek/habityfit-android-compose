package com.piodev.habityfit.features.account

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.piodev.habityfit.features.community.presentation.ui.RequestScreen
import com.piodev.habityfit.features.community.presentation.ui.FriendFormScreen
import com.piodev.habityfit.features.community.presentation.ui.FriendsScreen
import com.piodev.habityfit.features.community.presentation.ui.RoutineRequestScreen
import com.piodev.habityfit.features.community.presentation.ui.SentRequestsScreen
import com.piodev.habityfit.features.community.presentation.ui.SettingsScreen
import com.piodev.habityfit.features.exercises.presentation.ui.ExercisesScreen
import com.piodev.habityfit.features.informe.presentation.ui.InformeScreen
import com.piodev.habityfit.features.measurements.presentation.ui.MeasurementsEditScreen
import com.piodev.habityfit.features.measurements.presentation.ui.MeasurementsFormScreen
import com.piodev.habityfit.features.measurements.presentation.ui.MeasurementsScreen
import com.piodev.habityfit.features.rm.presentation.ui.RMScreen

@Composable
fun AccountMenuScreen(
    onLogout: () -> Unit
) {
    val profileNavController = rememberNavController()

    NavHost(
        navController = profileNavController,
        startDestination = "profile_menu"
    ) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Menu Principal del Perfil
        ////////////////////////////////////////////////////////////////////////////////////////////
        composable(route = "profile_menu") {
            AccountScreen(
                onNavigateTo = { route -> profileNavController.navigate(route) },
                onLogout = onLogout
            )
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Solicitudes de Rutina: Pantalla para mostrar las solicitudes de rutina recibidas
        ////////////////////////////////////////////////////////////////////////////////////////////
        composable(route = "routine_requests") {
            RoutineRequestScreen(
                onBack = { profileNavController.popBackStack() }
            )
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Ejercicios: Pantalla para mostrar los ejercicios disponibles
        ////////////////////////////////////////////////////////////////////////////////////////////
        composable(route = "exercise") {
            ExercisesScreen()
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Pantallas de ajustes de cuenta
        ////////////////////////////////////////////////////////////////////////////////////////////
        composable(route = "settings") {
            SettingsScreen(
                onBack = { profileNavController.popBackStack() }
            )
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 1RM: Calculadora de Repetición Máxima Estimada
        ////////////////////////////////////////////////////////////////////////////////////////////
        composable(route = "rm") {
            RMScreen(
                onBack = { profileNavController.popBackStack() }
            )
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Pantallas de Medidas Corporales
        ////////////////////////////////////////////////////////////////////////////////////////////
        composable(route = "measurements") {
            MeasurementsScreen(
                onAddMeasurement = { profileNavController.navigate("add_measurement") },
                onEditMeasurement = { id -> profileNavController.navigate("edit_measurement/$id") },
                onBack = { profileNavController.popBackStack() }
            )
        }
        composable(route = "add_measurement") {
            MeasurementsFormScreen(
                onBack = { profileNavController.popBackStack() }
            )
        }
        composable(
            route = "edit_measurement/{bodyId}",
            arguments = listOf(navArgument("bodyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("bodyId") ?: ""
            MeasurementsEditScreen(
                bodyId = id,
                onBack = { profileNavController.popBackStack() }
            )
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Pantallas de la comunidad
        ////////////////////////////////////////////////////////////////////////////////////////////
        composable(route = "community") {
            FriendsScreen(
                onAddFriend = { profileNavController.navigate("add_friend") },
                onSentRequests = { profileNavController.navigate("sent_requests") },
                onRequests = { profileNavController.navigate("requests_friends") },
                onBack = { profileNavController.popBackStack() }
            )
        }
        composable(route = "add_friend") {
            FriendFormScreen(
                onBack = { profileNavController.popBackStack() }
            )
        }
        composable(route = "requests_friends") {
            RequestScreen(
                onBack = { profileNavController.popBackStack() }
            )
        }
        composable(route = "sent_requests") {
            SentRequestsScreen(
                onBack = { profileNavController.popBackStack() }
            )
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Pantalla de información de la aplicación
        ////////////////////////////////////////////////////////////////////////////////////////////
        composable(route = "info") {
            InformeScreen(
                onBack = { profileNavController.popBackStack() }
            )
        }
    }
}