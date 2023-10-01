package mai.project.to_docompose.navigation

import androidx.navigation.NavHostController
import mai.project.to_docompose.util.Action
import mai.project.to_docompose.util.Constants.LIST_SCREEN

class Screens(navController: NavHostController) {

    val list: (action: Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }

    val task: (taskId: Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}