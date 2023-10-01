package mai.project.to_docompose.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mai.project.to_docompose.data.TodoDatabase
import mai.project.to_docompose.util.Constants.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context = context,
        klass = TodoDatabase::class.java,
        name = DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: TodoDatabase) = database.toDoDao()
}