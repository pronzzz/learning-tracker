package com.example.learningtracker

import android.app.Application
import androidx.room.Room
import com.example.learningtracker.data.AppDatabase
import com.example.learningtracker.data.repository.AppRepository

class LearningTrackerApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
        
        lateinit var repository: AppRepository
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "learning_tracker_db"
        ).fallbackToDestructiveMigration().build()
        
        repository = AppRepository(database.appDao())
    }
}
