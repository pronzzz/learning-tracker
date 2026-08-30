package com.example.learningtracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.learningtracker.data.dao.AppDao
import com.example.learningtracker.data.models.*

@Database(
    entities = [
        Topic::class,
        Project::class,
        Task::class,
        Resource::class,
        Note::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
