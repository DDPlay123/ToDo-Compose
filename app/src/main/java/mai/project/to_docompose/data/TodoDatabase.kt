package mai.project.to_docompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import mai.project.to_docompose.data.models.ToDoTask

@Database(
    entities = [ToDoTask::class],
    version = 1
)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun toDoDao(): ToDoDao
}