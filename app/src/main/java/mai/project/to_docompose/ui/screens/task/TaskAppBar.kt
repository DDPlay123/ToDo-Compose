@file:OptIn(ExperimentalMaterial3Api::class)

package mai.project.to_docompose.ui.screens.task

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import mai.project.to_docompose.R
import mai.project.to_docompose.components.DisplayAlertDialog
import mai.project.to_docompose.data.models.Priority
import mai.project.to_docompose.data.models.ToDoTask
import mai.project.to_docompose.ui.theme.topAppBarBackgroundColor
import mai.project.to_docompose.ui.theme.topAppBarContentColor
import mai.project.to_docompose.util.Action

@Composable
fun TaskAppBar(
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit
) {
    if (selectedTask == null) {
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    } else {
        ExistingTaskAppBar(
            selectedTask = selectedTask,
            navigateToListScreen = navigateToListScreen
        )
    }
}

@Composable
fun NewTaskAppBar(
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.add_task),
                color = topAppBarContentColor
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = topAppBarBackgroundColor
        ),
        navigationIcon = {
            BackAction(onBackClicked = navigateToListScreen)
        },
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }
    )
}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(
        onClick = { onBackClicked.invoke(Action.NO_ACTION) }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back),
            tint = topAppBarContentColor
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
) {
    IconButton(
        onClick = { onAddClicked.invoke(Action.ADD) }
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.add_task),
            tint = topAppBarContentColor
        )
    }
}

@Composable
fun ExistingTaskAppBar(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = selectedTask.title,
                color = topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = topAppBarBackgroundColor
        ),
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        actions = {
            ExistingTaskAppBarAction(
                selectedTask = selectedTask,
                navigateToListScreen = navigateToListScreen
            )
        }
    )
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(
        onClick = { onCloseClicked.invoke(Action.NO_ACTION) }
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = R.string.close),
            tint = topAppBarContentColor
        )
    }
}

@Composable
fun ExistingTaskAppBarAction(
    selectedTask: ToDoTask,
    navigateToListScreen: (Action) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(
            id = R.string.delete_task_title,
            selectedTask.title
        ),
        message = stringResource(
            id = R.string.delete_task_message,
            selectedTask.title
        ),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onConfirmClicked = { navigateToListScreen.invoke(Action.DELETE) }
    )

    DeleteAction(onCloseClicked = { openDialog = true })

    UpdateAction(onUpdateClicked = navigateToListScreen)
}

@Composable
fun DeleteAction(
    onCloseClicked: () -> Unit
) {
    IconButton(
        onClick = { onCloseClicked.invoke() }
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete),
            tint = topAppBarContentColor
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
) {
    IconButton(
        onClick = { onUpdateClicked.invoke(Action.UPDATE) }
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(id = R.string.update),
            tint = topAppBarContentColor
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NewTaskAppBarPreview() {
    NewTaskAppBar(navigateToListScreen = {})
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExistingAppBarPreview() {
    ExistingTaskAppBar(
        selectedTask = ToDoTask(
            id = 1,
            title = "Task",
            description = "Description",
            priority = Priority.HIGH
        ),
        navigateToListScreen = {}
    )
}