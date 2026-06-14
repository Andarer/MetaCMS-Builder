package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.CmsViewModel
import kotlinx.coroutines.launch

@Composable
fun GithubScreen(viewModel: CmsViewModel) {
    val currentProj by viewModel.currentProject.collectAsState()
    val gitLogs by viewModel.gitLogs.collectAsState()
    val isBuilding by viewModel.isBuilding.collectAsState()
    val buildOutput by viewModel.buildOutput.collectAsState()

    var commitMsg by remember { mutableStateOf("") }
    var showReleaseDialog by remember { mutableStateOf(false) }
    var releaseVersion by remember { mutableStateOf("v1.4.0") }
    var releaseNotes by remember { mutableStateOf("Localized update. Added multi-language engine support.") }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Autoscroll the logs list to the end on change
    LaunchedEffect(gitLogs.size) {
        if (gitLogs.isNotEmpty()) {
            listState.animateScrollToItem(gitLogs.size - 1)
        }
    }

    if (currentProj == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.Default.CloudQueue, 
                    contentDescription = null, 
                    modifier = Modifier.size(64.dp), 
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.t("specs_create_project_alert"), 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("github_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = viewModel.t("github_header_title"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = viewModel.t("github_header_desc"),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Repo details card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = viewModel.t("project_repo_label"),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currentProj!!.githubRepo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${viewModel.t("card_branch")}: origin/${currentProj!!.githubBranch}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isBuilding) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color(0xFF4CAF50).copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isBuilding) "BUILDING" else "DEPLOYED",
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = if (isBuilding) MaterialTheme.colorScheme.primary else Color(0xFF388E3C)
                    )
                }
            }
        }

        // Terminal Log viewer
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Workspace Git Console Terminal",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF151515))
                    .padding(8.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(gitLogs) { log ->
                        Text(
                            text = log,
                            color = if (log.contains("Error") || log.contains("fail")) {
                                Color(0xFFFF5252)
                            } else if (log.contains("[Git]")) {
                                Color(0xFF81C784)
                            } else if (log.contains("[System]")) {
                                Color(0xFF64B5F6)
                            } else {
                                Color(0xFFEEEEEE)
                            },
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    if (isBuilding && buildOutput.isNotEmpty()) {
                        item {
                            Divider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 4.dp))
                            Text(
                                text = buildOutput,
                                color = Color(0xFFFFD54F),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Commit input and quick command buttons tray
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = commitMsg,
                onValueChange = { commitMsg = it },
                label = { Text(viewModel.t("commit_msg_label"), fontSize = 12.sp) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        if (commitMsg.isNotBlank()) {
                            viewModel.gitCommit(commitMsg)
                            commitMsg = ""
                        }
                    }) {
                        Icon(Icons.Default.SaveAlt, contentDescription = "Commit changes")
                    }
                }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.gitPull() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    enabled = !isBuilding
                ) {
                    Icon(Icons.Default.CallReceived, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pull", fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.gitPush() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    enabled = !isBuilding
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Push & Deploy", fontSize = 12.sp)
                }

                Button(
                    onClick = { showReleaseDialog = true },
                    modifier = Modifier.weight(12f / 10f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    enabled = !isBuilding
                ) {
                    Icon(Icons.Default.NewReleases, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(viewModel.t("publish_release"), fontSize = 12.sp)
                }
            }
        }
    }

    if (showReleaseDialog) {
        AlertDialog(
            onDismissRequest = { showReleaseDialog = false },
            title = { Text(viewModel.t("publish_release_diag")) },
            confirmButton = {
                Button(onClick = {
                    if (releaseVersion.isNotBlank()) {
                        viewModel.gitCreateRelease(releaseVersion, releaseNotes)
                        showReleaseDialog = false
                    }
                }) {
                    Text(viewModel.t("btn_publish"))
                }
            },
            dismissButton = {
                TextButton(onClick = { showReleaseDialog = false }) {
                    Text(viewModel.t("btn_cancel"))
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = releaseVersion,
                        onValueChange = { releaseVersion = it },
                        label = { Text(viewModel.t("version_tag_label")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = releaseNotes,
                        onValueChange = { releaseNotes = it },
                        label = { Text(viewModel.t("changelog_field_label")) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}
