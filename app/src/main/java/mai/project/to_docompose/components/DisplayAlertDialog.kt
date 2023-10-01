package mai.project.to_docompose.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import mai.project.to_docompose.R

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmClicked.invoke()
                        closeDialog.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm)
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        closeDialog.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel)
                    )
                }
            },
            onDismissRequest = { closeDialog.invoke() }
        )
    }
}

@Preview
@Composable
private fun DisplayAlertDialogPreview() {
    DisplayAlertDialog(
        title = "Title",
        message = "Message",
        openDialog = true,
        closeDialog = {},
        onConfirmClicked = {}
    )
}