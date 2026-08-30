package com.example.learningtracker.widget

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.learningtracker.LearningTrackerApplication
import com.example.learningtracker.MainActivity
import com.example.learningtracker.data.models.Project
import kotlinx.coroutines.flow.first
import androidx.glance.appwidget.cornerRadius

class LearningTrackerWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Fetch data from database
        val repository = LearningTrackerApplication.repository
        
        // Use first() to just get the current snapshot of projects for the widget
        val activeProjects = try {
            repository.getAllProjects().first().take(3)
        } catch (e: Exception) {
            emptyList()
        }

        provideContent {
            GlanceTheme {
                WidgetContent(context = context, activeProjects = activeProjects)
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun WidgetContent(context: Context, activeProjects: List<Project>) {
    val intent = android.content.Intent(context, MainActivity::class.java).apply {
        flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.background)
            .padding(16.dp)
            .clickable(actionStartActivity(intent)), // Open app on click
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Learning Tracker",
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        
        Spacer(modifier = GlanceModifier.height(12.dp))
        
        if (activeProjects.isEmpty()) {
            Text(
                text = "No active projects.",
                style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant)
            )
        } else {
            activeProjects.forEach { project ->
                ProjectRow(project)
                Spacer(modifier = GlanceModifier.height(8.dp))
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun ProjectRow(project: Project) {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(GlanceTheme.colors.surfaceVariant)
            .cornerRadius(12.dp)
            .padding(12.dp)
    ) {
        Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = project.title,
                modifier = GlanceModifier.defaultWeight(),
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = "${(project.progress * 100).toInt()}%",
                style = TextStyle(
                    color = GlanceTheme.colors.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
