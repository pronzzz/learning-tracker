package com.example.learningtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningtracker.data.models.Project
import com.example.learningtracker.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectsViewModel(private val repository: AppRepository) : ViewModel() {

    private val _selectedTopicId = MutableStateFlow<Long?>(null)
    val selectedTopicId = _selectedTopicId.asStateFlow()

    // When topicId changes, this flow will update with the corresponding projects
    val projects: StateFlow<List<Project>> = _selectedTopicId
        .flatMapLatest { topicId ->
            if (topicId != null) {
                repository.getProjectsForTopic(topicId)
            } else {
                repository.getAllProjects() // Show all if no specific topic selected
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectTopic(topicId: Long?) {
        _selectedTopicId.value = topicId
    }

    fun setTopicFilter(topicId: Long?) {
        _selectedTopicId.value = topicId
    }

    fun addProject(topicId: Long, title: String, description: String, deadline: Long?) {
        viewModelScope.launch {
            val project = Project(
                topicId = topicId,
                title = title,
                description = description,
                deadline = deadline,
                progress = 0f,
                status = "Not Started"
            )
            repository.insertProject(project)
        }
    }

    fun updateProjectProgress(project: Project, newProgress: Float, newStatus: String) {
        viewModelScope.launch {
            repository.updateProject(project.copy(progress = newProgress, status = newStatus))
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            repository.deleteProject(project)
        }
    }
}
