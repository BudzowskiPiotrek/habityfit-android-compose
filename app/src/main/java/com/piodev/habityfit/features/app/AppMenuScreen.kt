package com.piodev.habityfit.features.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.piodev.habityfit.features.account.AccountMenuScreen
import com.piodev.habityfit.features.exercises.presentation.ui.ExercisesScreen
import com.piodev.habityfit.features.feed.presentation.ui.FeedScreen
import com.piodev.habityfit.features.graphics.presentation.ui.GraphicsScreen
import com.piodev.habityfit.features.routine.presentation.ui.ExerciseEditScreen
import com.piodev.habityfit.features.routine.presentation.ui.RoutineEditScreen
import com.piodev.habityfit.features.routine.presentation.ui.RoutineFormScreen
import com.piodev.habityfit.features.routine.presentation.ui.RoutineListScreen
import com.piodev.habityfit.features.routine.presentation.ui.RoutineScreen
import com.piodev.habityfit.features.workout.presentation.ui.SetEditScreen
import com.piodev.habityfit.features.workout.presentation.ui.WorkoutEditScreen
import com.piodev.habityfit.features.workout.presentation.ui.WorkoutFormScreen
import com.piodev.habityfit.features.workout.presentation.ui.WorkoutListScreen
import com.piodev.habityfit.features.workout.presentation.ui.WorkoutScreen

@Composable
fun AppMenuScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "feed",
        modifier = modifier
    ) {
        ////////////////////////////////////////////////////////////////////////////////////////
        // Ejercicios: Pantalla para mostrar los ejercicios disponibles
        ////////////////////////////////////////////////////////////////////////////////////////
        composable("exercises") {
            ExercisesScreen()
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        // Routina: Pantalla para mostrar las rutinas y agregar nueva rutina
        ////////////////////////////////////////////////////////////////////////////////////////
        composable("routines") {
            RoutineScreen(
                onAddRoutine = { navController.navigate("add_routine") },
                onRoutineClick = { routineId ->
                    navController.navigate("routine_details/$routineId")
                }, onEditRoutine = { routineId ->
                    navController.navigate("edit_routine/$routineId")
                }
            )
        }
        composable(
            route = "routine_details/{routineId}",
            arguments = listOf(navArgument("routineId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("routineId") ?: ""
            RoutineListScreen(
                routineId = id,
                onBack = { navController.popBackStack() },
                onEditExercise = { exerciseId ->
                    navController.navigate("edit_exercise/$exerciseId")
                },
                onNavigateToGraphics = { routineId ->
                    navController.navigate("graphics/$routineId")
                }
            )
        }
        composable(
            route = "graphics/{routineId}",
            arguments = listOf(navArgument("routineId") { type = NavType.StringType })
        ) {
            GraphicsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "edit_routine/{routineId}",
            arguments = listOf(navArgument("routineId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("routineId") ?: ""
            RoutineEditScreen(
                routineId = id,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "edit_exercise/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("exerciseId") ?: ""
            ExerciseEditScreen(
                exerciseId = id,
                onBack = { navController.popBackStack() }
            )
        }
        composable("add_routine") {
            RoutineFormScreen(
                onBack = { navController.popBackStack() }
            )
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        // Entrenar: Pantalla para mostrar el entrenamiento actual y registrar progreso
        ////////////////////////////////////////////////////////////////////////////////////////
        composable("workouts") {
            WorkoutScreen(
                onAddWorkout = {
                    navController.navigate("add_workout")
                },
                onSessionClick = { sessionId ->
                    navController.navigate("session_details/$sessionId")
                },
                onEditSession = { sessionId ->
                    navController.navigate("edit_session/$sessionId")
                }
            )
        }
        composable(
            route = "edit_session/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("sessionId") ?: ""
            WorkoutEditScreen(
                sessionId = id,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "session_details/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("sessionId") ?: ""
            WorkoutListScreen(
                sessionId = id,
                onBack = { navController.popBackStack() },
                onExerciseClick = { exerciseId ->
                    navController.navigate("set_edit/$exerciseId")
                }
            )
        }
        composable(
            route = "set_edit/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            SetEditScreen(
                exerciseId = exerciseId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("add_workout") {
            WorkoutFormScreen(
                onBack = { navController.popBackStack() }
            )
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        // Feed
        ////////////////////////////////////////////////////////////////////////////////////////
        composable("feed") {
            FeedScreen()
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        // Perfil: Pantalla para mostrar la información del usuario y opciones de configuración
        ////////////////////////////////////////////////////////////////////////////////////////
        composable("profile") {
            AccountMenuScreen(
                onLogout = onLogout
            )
        }
    }
}