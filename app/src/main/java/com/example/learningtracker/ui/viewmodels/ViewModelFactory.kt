package com.example.learningtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learningtracker.LearningTrackerApplication

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = LearningTrackerApplication.repository
        
        if (modelClass.isAssignableFrom(TopicsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopicsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ProjectsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
