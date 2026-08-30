package com.example.learningtracker.data.dao

import androidx.room.*
import com.example.learningtracker.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Topics
    @Query("SELECT * FROM topics ORDER BY createdAt DESC")
    fun getAllTopics(): Flow<List<Topic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopic(topic: Topic)

    @Delete
    fun deleteTopic(topic: Topic)

    // Projects
    @Query("SELECT * FROM projects WHERE topicId = :topicId ORDER BY deadline ASC")
    fun getProjectsForTopic(topicId: Long): Flow<List<Project>>

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProject(project: Project)

    @Update
    fun updateProject(project: Project)

    @Delete
    fun deleteProject(project: Project)

    // Tasks
    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY isCompleted ASC, dueDate ASC")
    fun getTasksForProject(projectId: Long): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    // Resources
    @Query("SELECT * FROM resources WHERE projectId = :projectId")
    fun getResourcesForProject(projectId: Long): Flow<List<Resource>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResource(resource: Resource)

    @Update
    fun updateResource(resource: Resource)

    @Delete
    fun deleteResource(resource: Resource)

    // Notes
    @Query("SELECT * FROM notes WHERE projectId = :projectId ORDER BY timestamp DESC")
    fun getNotesForProject(projectId: Long): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)
}
