@file:OptIn(ExperimentalMaterial3Api::class)

package mai.project.to_docompose.ui.screens.list

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mai.project.to_docompose.R
import mai.project.to_docompose.components.DisplayAlertDialog
import mai.project.to_docompose.data.models.Priority
import mai.project.to_docompose.ui.theme.PRIORITY_INDICATOR_SIZE
import mai.project.to_docompose.ui.theme.TOP_APP_BAR_HEIGHT
import mai.project.to_docompose.ui.theme.topAppBarBackgroundColor
import mai.project.to_docompose.ui.theme.topAppBarContentColor
import mai.project.to_docompose.ui.viewmodels.SharedViewModel
import mai.project.to_docompose.util.Action
import mai.project.to_docompose.util.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String,
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.updateAppBarState(newSearchAppBarState = SearchAppBarState.OPENED)
                },
                onSortClicked = {
                    sharedViewModel.persistSorting(it)
                },
                onDeleteAllConfirmed = {
                    sharedViewModel.updateAction(newAction = Action.DELETE_ALL)
                }
            )
        }

        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = {
                    sharedViewModel.updateSearchText(newSearchText = it)
                },
                onCloseClicked = {
                    sharedViewModel.updateAppBarState(newSearchAppBarState = SearchAppBarState.CLOSED)
                    sharedViewModel.updateSearchText(newSearchText = "")
                },
                onSearchClicked = {
                    sharedViewModel.searchDatabase(searchQuery = it)
                }
            )
        }
    }
}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.tasks),
                color = topAppBarContentColor
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = topAppBarBackgroundColor
        ),
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(id = R.string.delete_all_tasks),
        message = stringResource(id = R.string.delete_all_tasks_message),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onConfirmClicked = { onDeleteAllConfirmed.invoke() }
    )

    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllConfirmed = { openDialog = true })
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(
        onClick = onSearchClicked
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_tasks),
            tint = topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_filter_list_24),
            contentDescription = stringResource(id = R.string.sort_tasks),
            tint = topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Priority.values().slice(setOf(0, 2, 3)).forEach { priority ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = priority.name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    leadingIcon = {
                        Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
                            drawCircle(color = priority.color)
                        }
                    },
                    onClick = {
                        expanded = false
                        onSortClicked.invoke(priority)
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_more_vert_24),
            contentDescription = stringResource(id = R.string.delete_all_tasks),
            tint = topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.delete_all_tasks),
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                onClick = {
                    expanded = false
                    onDeleteAllConfirmed.invoke()
                }
            )
        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        shadowElevation = 4.dp,
        color = topAppBarBackgroundColor
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search),
                    color = Color.White,
                    fontWeight = FontWeight.Normal
                )
            },
            textStyle = TextStyle(
                color = topAppBarContentColor,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ) {
                    IconButton(
                        onClick = {
                            onSearchClicked.invoke(text)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search),
                            tint = topAppBarContentColor
                        )
                    }
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty())
                            onTextChange.invoke("")
                        else
                            onCloseClicked.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close),
                        tint = topAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked.invoke(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent
            )
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteAllConfirmed = {}
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SearchAppBarPreview() {
    SearchAppBar(
        text = "Search",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}