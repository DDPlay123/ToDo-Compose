package mai.project.to_docompose.navigation.destinations

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import mai.project.to_docompose.ui.screens.task.TaskScreen
import mai.project.to_docompose.ui.viewmodels.SharedViewModel
import mai.project.to_docompose.util.Action
import mai.project.to_docompose.util.Constants.TASK_ARGUMENT_KEY
import mai.project.to_docompose.util.Constants.TASK_SCREEN

fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (action: Action) -> Unit
) {
    composable(
        route = TASK_SCREEN,
        arguments = listOf(navArgument(TASK_ARGUMENT_KEY) {
            type = NavType.IntType
        }),
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) { navBackStackEntry ->
        val taskId = navBackStackEntry.arguments?.getInt(TASK_ARGUMENT_KEY) ?: 0
        LaunchedEffect(key1 = taskId) {
            sharedViewModel.getSelectedTask(taskId = taskId)
        }

        val selectedTask by sharedViewModel.selectedTask.collectAsState()
        // 注：如果 key1 使用 taskId 會有Bug，因為 taskId 會優先於 selectedTask 被更新
        // 所以在這裡使用 selectedTask 作為 key1，避免產生競爭條件。
        LaunchedEffect(key1 = selectedTask) {
            if (selectedTask != null || taskId == -1)
                sharedViewModel.updateTaskFields(selectedTask = selectedTask)
        }

        TaskScreen(
            navigateToListScreen = navigateToListScreen,
            sharedViewModel = sharedViewModel,
            selectedTask = selectedTask
        )
    }
}