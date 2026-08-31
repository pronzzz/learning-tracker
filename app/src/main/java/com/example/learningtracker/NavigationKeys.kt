package com.example.learningtracker

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object DashboardRoute : NavKey
@Serializable data object TopicsRoute : NavKey
@Serializable data class ProjectsRoute(val topicId: Long? = null) : NavKey
@Serializable data class ProjectDetailRoute(val projectId: Long) : NavKey
