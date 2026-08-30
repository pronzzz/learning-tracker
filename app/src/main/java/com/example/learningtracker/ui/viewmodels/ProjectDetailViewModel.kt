package com.example.learningtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningtracker.data.models.Note
import com.example.learningtracker.data.models.Project
import com.example.learningtracker.data.models.Resource
import com.example.learningtracker.data.models.Task
import com.example.learningtracker.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProjectDetailState(
    val project: Project? = null,
    val tasks: List<Task> = emptyList(),
    val resources: List<Resource> = emptyList(),
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = true
)

class ProjectDetailViewModel(private val repository: AppRepository) : ViewModel() {

    private val _projectId = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<ProjectDetailState> = _projectId
        .flatMapLatest { id ->
            if (id != null) {
                combine(
                    repository.getAllProjects(), // A bit inefficient, but works for now to find the project
                    repository.getTasksForProject(id),
                    repository.getResourcesForProject(id),
                    repository.getNotesForProject(id)
                ) { projects, tasks, resources, notes ->
                    ProjectDetailState(
                        project = projects.find { it.id == id },
                        tasks = tasks,
                        resources = resources,
                        notes = notes,
                        isLoading = false
                    )
                }
            } else {
                MutableStateFlow(ProjectDetailState(isLoading = false))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProjectDetailState(isLoading = true)
        )

    fun loadProject(projectId: Long) {
        _projectId.value = projectId
    }

    fun addTask(title: String, dueDate: Long?, priority: Int) {
        val pid = _projectId.value ?: return
        viewModelScope.launch {
            repository.insertTask(
                Task(
                    projectId = pid,
                    title = title,
                    isCompleted = false,
                    dueDate = dueDate,
                    priority = priority
                )
            )
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
    
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun addResource(title: String, type: String, url: String) {
        val pid = _projectId.value ?: return
        viewModelScope.launch {
            repository.insertResource(
                Resource(
                    projectId = pid,
                    title = title,
                    type = type,
                    url = url,
                    isCompleted = false,
                    progress = 0f
                )
            )
        }
    }
    
    fun deleteResource(resource: Resource) {
        viewModelScope.launch {
            repository.deleteResource(resource)
        }
    }

    fun addNote(content: String) {
        val pid = _projectId.value ?: return
        viewModelScope.launch {
            repository.insertNote(
                Note(
                    projectId = pid,
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
    
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}
