package mai.project.to_docompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import mai.project.to_docompose.navigation.destinations.listComposable
import mai.project.to_docompose.navigation.destinations.taskComposable
import mai.project.to_docompose.ui.viewmodels.SharedViewModel
import mai.project.to_docompose.util.Constants.LIST_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(
        navController = navController,
        startDestination = LIST_SCREEN
    ) {
        listComposable(
            navigateToTaskScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            navigateToListScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
    }
}