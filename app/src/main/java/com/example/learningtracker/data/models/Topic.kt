package com.example.learningtracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: String, // hex string
    val icon: String,
    val createdAt: Long = System.currentTimeMillis()
)
