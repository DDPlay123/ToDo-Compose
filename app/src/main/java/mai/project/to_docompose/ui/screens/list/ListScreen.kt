package mai.project.to_docompose.ui.screens.list

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import mai.project.to_docompose.R
import mai.project.to_docompose.ui.theme.fabBackgroundColor
import mai.project.to_docompose.ui.theme.fabTintColor
import mai.project.to_docompose.ui.viewmodels.SharedViewModel
import mai.project.to_docompose.util.Action
import mai.project.to_docompose.util.SearchAppBarState

@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action = action)
    }

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState: String = sharedViewModel.searchTextState

    val snackbarHostState = remember { SnackbarHostState() }

    DisplaySnackBar(
        snackbarHostState = snackbarHostState,
        onComplete = { sharedViewModel.updateAction(newAction = it) },
        onUndoClicked = { sharedViewModel.updateAction(newAction = it) },
        taskTitle = sharedViewModel.title,
        action = action
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = { padding ->
            ListContent(
                modifier = Modifier.padding(padding),
                allTasks = allTasks,
                searchedTasks = searchedTasks,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, task ->
                    sharedViewModel.updateAction(newAction = action)
                    sharedViewModel.updateTaskFields(selectedTask = task)
                    snackbarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        }, containerColor = fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_task),
            tint = fabTintColor
        )
    }
}

@Composable
fun DisplaySnackBar(
    snackbarHostState: SnackbarHostState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = setMessage(context = context, action = action, taskTitle = taskTitle),
                    actionLabel = setActionLabel(context = context, action = action),
                    duration = SnackbarDuration.Short
                )

                if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE)
                    onUndoClicked.invoke(Action.UNDO)
                else if (snackBarResult == SnackbarResult.Dismissed || action != Action.DELETE)
                    onComplete.invoke(Action.NO_ACTION)
            }
        }
    }
}

private fun setMessage(
    context: Context,
    action: Action,
    taskTitle: String
): String {
    return if (action == Action.DELETE_ALL)
        context.getString(R.string.delete_all)
    else
        "${action.name}: $taskTitle"
}

private fun setActionLabel(
    context: Context,
    action: Action
): String {
    return if (action == Action.DELETE)
        context.getString(R.string.cancel)
    else
        context.getString(R.string.confirm)
}
