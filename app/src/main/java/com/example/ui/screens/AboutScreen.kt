package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

@Composable
fun AboutScreen(viewModel: CmsViewModel) {
    val currentProj by viewModel.currentProject.collectAsState()

    var activeTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        viewModel.t("about_tab_tz"), 
        viewModel.t("about_tab_architecture"), 
        viewModel.t("about_tab_roadmap")
    )

    // Local roadmaps states representing interactive features
    var item1 by remember { mutableStateOf(true) }
    var item2 by remember { mutableStateOf(true) }
    var item3 by remember { mutableStateOf(true) }
    var item4 by remember { mutableStateOf(true) }
    var item5 by remember { mutableStateOf(false) }
    var item6 by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("about_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = viewModel.t("about_header_title"),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = viewModel.t("about_header_desc"),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Horizontal tabs switcher
        TabRow(selectedTabIndex = activeTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == index,
                    onClick = { activeTab = index },
                    text = { Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (activeTab) {
                0 -> { // README & Docs
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "MetaCMS Builder Engine v1.3.0",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Multilingual Localization Dynamic Engine",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Text(
                                    text = viewModel.t("about_engine_desc"),
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )

                                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                                Text(viewModel.t("key_features_title"), fontWeight = FontWeight.Bold, fontSize = 14.sp)

                                BulletPoint(viewModel.t("key_feature_1"), color = MaterialTheme.colorScheme.primary)
                                BulletPoint(viewModel.t("key_feature_2"), color = MaterialTheme.colorScheme.primary)
                                BulletPoint(viewModel.t("key_feature_3"), color = MaterialTheme.colorScheme.primary)
                                BulletPoint(viewModel.t("key_feature_4"), color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    item {
                        Text(viewModel.t("deploy_pwa_title"), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = """
                                    # Конфигурация Workbox Caching (PWA):
                                    // modules/pwa_config.js
                                    workbox.routing.registerRoute(
                                      new RegExp('/cms-api/'),
                                      new workbox.strategies.NetworkFirst({
                                        cacheName: 'metacms-cache-v1'
                                      })
                                    );
                                    
                                    # Запуск Multi-stage Docker Контейнера:
                                    $ docker build -f docker/Dockerfile -t metacms-site .
                                    $ docker compose up -d
                                    """.trimIndent(),
                                    color = Color(0xFFA9DFBF),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }

                1 -> { // Architecture Layout Details
                    item {
                        Text(
                            text = viewModel.t("architecture_flow_title"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                FolderElement("/app", viewModel.t("folder_app"), color = MaterialTheme.colorScheme.primary)
                                FolderElement("/core", viewModel.t("folder_core"), color = MaterialTheme.colorScheme.primary)
                                FolderElement("/builder", viewModel.t("folder_builder"), color = MaterialTheme.colorScheme.primary)
                                FolderElement("/modules", viewModel.t("folder_modules"), color = MaterialTheme.colorScheme.primary)
                                FolderElement("/docs", viewModel.t("folder_docs"), color = MaterialTheme.colorScheme.primary)
                                FolderElement("/docker", viewModel.t("folder_docker"), color = MaterialTheme.colorScheme.primary)
                                FolderElement("/.github", viewModel.t("folder_github"), color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    item {
                        Text(
                            text = viewModel.t("flow_diagram_label"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Android Visual DB Model (Room SQLite) --> Save Preferences (Multilang & Styles) --> Simulate local Database (Visual Sandbox) --> Export Web PWA & Docker specs --> Trigger deploy.yml Actions --> Publish on gh-pages!",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                2 -> { // Roadmap & Interactive Milestone Checklists
                    item {
                        Text(
                            text = viewModel.t("roadmap_title"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    item {
                        Card {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(viewModel.t("roadmap_subtitle"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item1, onCheckedChange = { item1 = it })
                                    Text("v1.0.0: ${viewModel.t("roadmap_v1")}", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item2, onCheckedChange = { item2 = it })
                                    Text("v1.1.0: ${viewModel.t("roadmap_v2")}", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item3, onCheckedChange = { item3 = it })
                                    Text("v1.2.0: ${viewModel.t("roadmap_v3")}", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item4, onCheckedChange = { item4 = it })
                                    Text("v1.3.0: ${viewModel.t("roadmap_v4")}", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item5, onCheckedChange = { item5 = it })
                                    Text("v1.5.0: ${viewModel.t("roadmap_v5")}", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item6, onCheckedChange = { item6 = it })
                                    Text("v2.0.0 Global: ${viewModel.t("roadmap_v6")}", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    item {
                        Text(viewModel.t("changelog_title"), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("v1.3.0 Release (Текущий)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.tertiary)
                                BulletPoint(viewModel.t("changelog_v1_3_1"), color = MaterialTheme.colorScheme.primary)
                                BulletPoint(viewModel.t("changelog_v1_3_2"), color = MaterialTheme.colorScheme.primary)
                                BulletPoint(viewModel.t("changelog_v1_3_3"), color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("v1.0.0 Core Build", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                BulletPoint(viewModel.t("changelog_v1_0_1"), color = MaterialTheme.colorScheme.primary)
                                BulletPoint(viewModel.t("changelog_v1_0_2"), color = MaterialTheme.colorScheme.primary)
                                BulletPoint(viewModel.t("changelog_v1_0_3"), color = MaterialTheme.colorScheme.primary)
                                BulletPoint(viewModel.t("changelog_v1_0_4"), color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BulletPoint(text: String, color: Color = MaterialTheme.colorScheme.primary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("•", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = color)
        Text(text = text, fontSize = 12.sp)
    }
}

@Composable
fun FolderElement(name: String, desc: String, color: Color = MaterialTheme.colorScheme.primary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(18.dp), tint = color)
        Column {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
            Text(desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
