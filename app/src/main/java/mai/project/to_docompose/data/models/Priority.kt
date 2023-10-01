package mai.project.to_docompose.data.models

import androidx.compose.ui.graphics.Color
import mai.project.to_docompose.ui.theme.HighPriorityColor
import mai.project.to_docompose.ui.theme.LowPriorityColor
import mai.project.to_docompose.ui.theme.MediumPriorityColor
import mai.project.to_docompose.ui.theme.NonePriorityColor

enum class Priority(
    val color: Color
) {
    HIGH(color = HighPriorityColor),
    MEDIUM(color = MediumPriorityColor),
    LOW(color = LowPriorityColor),
    NONE(color = NonePriorityColor)
}