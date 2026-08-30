package com.example.learningtracker

import android.app.Application

class LearningTrackerApplication : Application() {
    companion object {
        lateinit var database: com.example.learningtracker.data.AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = androidx.room.Room.databaseBuilder(
            this,
            com.example.learningtracker.data.AppDatabase::class.java,
            "learning_tracker_db"
        ).build()
    }
}
