package com.example.learningtracker.data.repository

import com.example.learningtracker.data.dao.AppDao
import com.example.learningtracker.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppRepository(private val appDao: AppDao) {

    // Topics
    fun getAllTopics(): Flow<List<Topic>> = appDao.getAllTopics()

    suspend fun insertTopic(topic: Topic) {
        withContext(Dispatchers.IO) {
            appDao.insertTopic(topic)
        }
    }

    suspend fun deleteTopic(topic: Topic) {
        withContext(Dispatchers.IO) {
            appDao.deleteTopic(topic)
        }
    }

    // Projects
    fun getProjectsForTopic(topicId: Long): Flow<List<Project>> = appDao.getProjectsForTopic(topicId)

    fun getAllProjects(): Flow<List<Project>> = appDao.getAllProjects()

    suspend fun insertProject(project: Project) {
        withContext(Dispatchers.IO) {
            appDao.insertProject(project)
        }
    }

    suspend fun updateProject(project: Project) {
        withContext(Dispatchers.IO) {
            appDao.updateProject(project)
        }
    }

    suspend fun addTimeSpentToProject(projectId: Long, durationMillis: Long) {
        withContext(Dispatchers.IO) {
            appDao.addTimeSpentToProject(projectId, durationMillis)
        }
    }
        withContext(Dispatchers.IO) {
            appDao.updateProject(project)
        }
    }

    suspend fun deleteProject(project: Project) {
        withContext(Dispatchers.IO) {
            appDao.deleteProject(project)
        }
    }

    // Tasks
    fun getTasksForProject(projectId: Long): Flow<List<Task>> = appDao.getTasksForProject(projectId)

    suspend fun insertTask(task: Task) {
        withContext(Dispatchers.IO) {
            appDao.insertTask(task)
        }
    }

    suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            appDao.updateTask(task)
        }
    }

    suspend fun deleteTask(task: Task) {
        withContext(Dispatchers.IO) {
            appDao.deleteTask(task)
        }
    }

    // Resources
    fun getResourcesForProject(projectId: Long): Flow<List<Resource>> = appDao.getResourcesForProject(projectId)

    suspend fun insertResource(resource: Resource) {
        withContext(Dispatchers.IO) {
            appDao.insertResource(resource)
        }
    }

    suspend fun updateResource(resource: Resource) {
        withContext(Dispatchers.IO) {
            appDao.updateResource(resource)
        }
    }

    suspend fun deleteResource(resource: Resource) {
        withContext(Dispatchers.IO) {
            appDao.deleteResource(resource)
        }
    }

    // Notes
    fun getNotesForProject(projectId: Long): Flow<List<Note>> = appDao.getNotesForProject(projectId)

    suspend fun insertNote(note: Note) {
        withContext(Dispatchers.IO) {
            appDao.insertNote(note)
        }
    }

    suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            appDao.deleteNote(note)
        }
    }
}
