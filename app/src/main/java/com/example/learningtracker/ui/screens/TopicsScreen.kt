package com.example.learningtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningtracker.data.models.Topic
import com.example.learningtracker.ui.viewmodels.TopicsViewModel
import com.example.learningtracker.ui.viewmodels.ViewModelFactory

@Composable
fun TopicsScreen(
    modifier: Modifier = Modifier,
    onTopicClick: (Long) -> Unit = {},
    viewModel: TopicsViewModel = viewModel(factory = ViewModelFactory())
) {
    val topics by viewModel.topics.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Topics",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                if (topics.isEmpty()) {
                    item {
                        Text(
                            text = "No topics yet. Add one below!",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                items(items = topics, key = { it.id }) { topic ->
                    TopicCard(
                        topic = topic,
                        onClick = { onTopicClick(topic.id) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
        
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .padding(bottom = 80.dp), // Clear bottom nav
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Topic")
        }
    }

    if (showAddDialog) {
        AddTopicDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, color, icon ->
                viewModel.addTopic(name, color, icon)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TopicCard(topic: Topic, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon Placeholder Box using color
            val colorInt = topic.color.toLongOrNull(16)?.toInt() ?: 0xFF000000.toInt()
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(colorInt)),
                contentAlignment = Alignment.Center
            ) {
                Text(topic.icon, fontSize = 24.sp)
            }
            
            Column {
                Text(topic.name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                // Future: Count active projects via relationship
                Text("Tap to view projects", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun AddTopicDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("📚") }
    // Default One UI blue
    var color by remember { mutableStateOf("FF3B82F6") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Topic") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Topic Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = icon,
                        onValueChange = { if (it.length <= 2) icon = it },
                        label = { Text("Emoji") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    OutlinedTextField(
                        value = color,
                        onValueChange = { if (it.length <= 8) color = it },
                        label = { Text("Hex Color") },
                        singleLine = true,
                        modifier = Modifier.weight(2f),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) onConfirm(name, color, icon)
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(28.dp)
    )
}
