package com.example.learningtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningtracker.ui.viewmodels.ProjectDetailViewModel
import com.example.learningtracker.ui.viewmodels.ViewModelFactory
import com.example.learningtracker.data.models.Task
import com.example.learningtracker.data.models.Resource
import com.example.learningtracker.data.models.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectDetailViewModel = viewModel(factory = ViewModelFactory())
) {
    // Tell ViewModel to load data for this specific project
    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tasks", "Resources", "Notes")
    
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showAddResourceDialog by remember { mutableStateOf(false) }
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var showTimerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        val project = state.project
                        Text(
                            project?.title ?: "Loading...", 
                            maxLines = 1, 
                            fontWeight = FontWeight.SemiBold
                        )
                        if (project != null) {
                            val hours = project.timeSpentMillis / (1000 * 60 * 60)
                            val minutes = (project.timeSpentMillis / (1000 * 60)) % 60
                            Text(
                                "${hours}h ${minutes}m spent", 
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.project != null) {
                        TextButton(onClick = { showTimerDialog = true }) {
                            Text("START SESSION")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    when(selectedTabIndex) {
                        0 -> showAddTaskDialog = true
                        1 -> showAddResourceDialog = true
                        2 -> showAddNoteDialog = true
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    when (selectedTabIndex) {
                        0 -> TasksTab(tasks = state.tasks, onToggle = { viewModel.toggleTaskCompletion(it) })
                        1 -> ResourcesTab(resources = state.resources)
                        2 -> NotesTab(notes = state.notes)
                    }
                }
            }
        }
    }

    // Dialogs
    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { title -> 
                viewModel.addTask(title, null, 1)
                showAddTaskDialog = false
            }
        )
    }
    
    if (showAddResourceDialog) {
        AddResourceDialog(
            onDismiss = { showAddResourceDialog = false },
            onConfirm = { title, url ->
                viewModel.addResource(title, "Link", url)
                showAddResourceDialog = false
            }
        )
    }
    
    if (showAddNoteDialog) {
        AddNoteDialog(
            onDismiss = { showAddNoteDialog = false },
            onConfirm = { content ->
                viewModel.addNote(content)
                showAddNoteDialog = false
            }
        )
    }
    
    if (showTimerDialog) {
        TimerDialog(
            onDismiss = { showTimerDialog = false },
            onSaveTime = { timeMillis ->
                viewModel.addTime(timeMillis)
                showTimerDialog = false
            }
        )
    }
}

@Composable
fun TasksTab(tasks: List<Task>, onToggle: (Task) -> Unit) {
    if (tasks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No tasks yet", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
        return
    }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = tasks, key = { it.id }) { task ->
            Row(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle(task) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ResourcesTab(resources: List<Resource>) {
    if (resources.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No resources yet", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
        return
    }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = resources, key = { it.id }) { resource ->
            Row(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Link, contentDescription = "Link", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(resource.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(resource.url ?: "", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun NotesTab(notes: List<Note>) {
    if (notes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No notes yet", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
        return
    }
    
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = notes, key = { it.id }) { note ->
            val dateStr = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(note.timestamp))
            Column(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(16.dp)
            ) {
                Text(dateStr, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Text(note.content, fontSize = 15.sp)
            }
        }
    }
}


@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var title by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Task") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Name") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        },
        confirmButton = {
            Button(onClick = { if (title.isNotBlank()) onConfirm(title) }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddResourceDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Resource") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL / Link") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = { if (title.isNotBlank() && url.isNotBlank()) onConfirm(title, url) }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddNoteDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var content by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Note") },
        text = {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Note content") },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp)
            )
        },
        confirmButton = {
            Button(onClick = { if (content.isNotBlank()) onConfirm(content) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun TimerDialog(onDismiss: () -> Unit, onSaveTime: (Long) -> Unit) {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedMillis by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            val startTime = System.currentTimeMillis() - elapsedMillis
            while (true) {
                elapsedMillis = System.currentTimeMillis() - startTime
                kotlinx.coroutines.delay(100L) // Update every 100ms
            }
        }
    }

    AlertDialog(
        onDismissRequest = { /* Prevent accidental dismiss */ },
        title = { Text("Study Session") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val totalSeconds = elapsedMillis / 1000
                val hours = totalSeconds / 3600
                val minutes = (totalSeconds % 3600) / 60
                val seconds = totalSeconds % 60
                
                Text(
                    text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { isRunning = !isRunning }) {
                        Text(if (isRunning) "Pause" else "Start")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { 
                onSaveTime(elapsedMillis)
                onDismiss() 
            }) { Text("Save & Exit") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
