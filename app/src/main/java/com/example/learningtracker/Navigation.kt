package com.example.learningtracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.learningtracker.ui.screens.DashboardScreen
import com.example.learningtracker.ui.screens.ProjectsScreen
import com.example.learningtracker.ui.screens.TopicsScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(DashboardRoute)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                val currentRoute = backStack.lastOrNull()
                NavigationBarItem(
                    selected = currentRoute == DashboardRoute,
                    onClick = { backStack.clear(); backStack.add(DashboardRoute) },
                    icon = { Text("D") }, // Placeholder icons
                    label = { Text("Dashboard") }
                )
                NavigationBarItem(
                    selected = currentRoute == TopicsRoute,
                    onClick = { backStack.clear(); backStack.add(TopicsRoute) },
                    icon = { Text("T") },
                    label = { Text("Topics") }
                )
                NavigationBarItem(
                    selected = currentRoute is ProjectsRoute,
                    onClick = { backStack.clear(); backStack.add(ProjectsRoute()) },
                    icon = { Text("P") },
                    label = { Text("Projects") }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<DashboardRoute> { 
                    DashboardScreen(
                        modifier = Modifier.padding(innerPadding),
                        onProjectClick = { projectId -> backStack.add(ProjectDetailRoute(projectId)) }
                    ) 
                }
                entry<TopicsRoute> { 
                    TopicsScreen(
                        modifier = Modifier.padding(innerPadding),
                        onTopicClick = { topicId -> backStack.add(ProjectsRoute(topicId)) }
                    ) 
                }
                entry<ProjectsRoute> { route ->
                    ProjectsScreen(
                        modifier = Modifier.padding(innerPadding),
                        topicId = route.topicId,
                        onProjectClick = { projectId -> backStack.add(ProjectDetailRoute(projectId)) }
                    ) 
                }
                entry<ProjectDetailRoute> { route ->
                    com.example.learningtracker.ui.screens.ProjectDetailScreen(
                        projectId = route.projectId,
                        onNavigateBack = { backStack.removeLastOrNull() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            },
        )
    }
}
