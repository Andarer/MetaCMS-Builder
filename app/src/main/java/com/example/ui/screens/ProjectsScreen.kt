package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CmsProject
import com.example.viewmodel.CmsViewModel
import com.example.viewmodel.ProjectsUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectsScreen(viewModel: CmsViewModel) {
    val projectsState by viewModel.projectsState.collectAsState()
    val activeProj by viewModel.currentProject.collectAsState()
    val context = LocalContext.current

    var showCreateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("projects_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = viewModel.t("projects_header"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = viewModel.t("projects_desc"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { showCreateDialog = true },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create")
                Spacer(modifier = Modifier.width(4.dp))
                Text(viewModel.t("add_field_btn"))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = projectsState) {
            is ProjectsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ProjectsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }
            is ProjectsUiState.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.projects) { proj ->
                        val isSelected = activeProj?.id == proj.id
                        ProjectItemCard(
                            project = proj,
                            isSelected = isSelected,
                            viewModel = viewModel,
                            onSelect = { viewModel.selectProject(proj) },
                            onDelete = { viewModel.deleteProject(proj) }
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateProjectDialog(
            viewModel = viewModel,
            onDismiss = { showCreateDialog = false },
            onCreate = { name, desc, techStack, repo, branch ->
                if (name.isBlank() || repo.isBlank()) {
                    Toast.makeText(context, viewModel.t("toast_fill_fields"), Toast.LENGTH_LONG).show()
                } else {
                    viewModel.createProject(name, desc, techStack, repo, branch)
                    showCreateDialog = false
                }
            }
        )
    }
}

@Composable
fun ProjectItemCard(
    project: CmsProject,
    isSelected: Boolean,
    viewModel: CmsViewModel,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    val borderStroke = if (isSelected) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    }

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickableRipple(onClick = onSelect),
        border = borderStroke,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = project.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Стек: ${project.techStack}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${viewModel.t("card_branch")}: origin/${project.githubBranch}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                val formattedDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(project.createdAt))
                Text(
                    text = "$formattedDate",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateProjectDialog(
    viewModel: CmsViewModel,
    onDismiss: () -> Unit,
    onCreate: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var repo by remember { mutableStateOf("andarer/my-awesome-cms") }
    var branch by remember { mutableStateOf("main") }

    val techStacks = listOf("Flutter App Suite", "Flutter Web PWA", "Headless CMS Portal")
    var selectedTechStack by remember { mutableStateOf(techStacks[0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(viewModel.t("create_new_project")) },
        confirmButton = {
            Button(
                onClick = {
                    onCreate(name, description, selectedTechStack, repo, branch)
                }
            ) {
                Text(viewModel.t("btn_add"))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(viewModel.t("btn_cancel"))
            }
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(viewModel.t("project_name_label")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(viewModel.t("project_desc_label")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Text(viewModel.t("tech_stack_label"), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        techStacks.forEach { stack ->
                            FilterChip(
                                selected = selectedTechStack == stack,
                                onClick = { selectedTechStack = stack },
                                label = { Text(stack, fontSize = 11.sp) }
                            )
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = repo,
                        onValueChange = { repo = it },
                        label = { Text(viewModel.t("github_repo_label")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        value = branch,
                        onValueChange = { branch = it },
                        label = { Text(viewModel.t("github_branch_label")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }
    )
}

@Composable
fun Modifier.clickableRipple(onClick: () -> Unit): Modifier {
    return this.clickable(
        onClick = onClick,
        interactionSource = null,
        indication = LocalIndication.current
    )
}
