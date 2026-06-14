package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CmsProject
import com.example.viewmodel.CmsViewModel
import com.example.viewmodel.ProjectsUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    viewModel: CmsViewModel,
    onNavigateToModules: () -> Unit,
    onNavigateToGithub: () -> Unit
) {
    val currentProj by viewModel.currentProject.collectAsState()
    val isBuilding by viewModel.isBuilding.collectAsState()

    var showDocDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = "MetaCMS Builder",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = viewModel.t("metacms_subtitle"),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Active project selector card or loading state
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewModel.t("selected_project_title"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentProj?.techStack ?: viewModel.t("none_project"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentProj?.name ?: viewModel.t("projects_empty"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Text(
                        text = currentProj?.description ?: viewModel.t("projects_empty_desc"),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (currentProj != null) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val activeModules = currentProj!!.activeModules.split(",").filter { it.isNotEmpty() }
                            activeModules.forEach { mod ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(mod, fontSize = 11.sp) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = getIconForModule(mod),
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Stats grid
        item {
            Text(
                text = viewModel.t("system_metrics"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Modules count
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(
                            imageVector = Icons.Default.ViewModule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${currentProj?.activeModules?.split(",")?.filter { it.isNotEmpty() }?.size ?: 0}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = viewModel.t("active_modules"), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Dynamic Database fields
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${viewModel.getSchemaFields().size}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = viewModel.t("database_fields"), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Live Build & Actions Status
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = viewModel.t("github_status_title"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(if (isBuilding) MaterialTheme.colorScheme.primary else Color(0xFF4CAF50))
                        )
                        Text(
                            text = if (isBuilding) viewModel.t("compilation_export") else viewModel.t("deployed_successfully"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${viewModel.t("hosts")}: https://${currentProj?.githubRepo ?: "github"}.github.io/site",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onNavigateToGithub,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(viewModel.t("git_console_btn"), fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { showDocDialog = true },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.AutoStories, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(viewModel.t("documents_btn"), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Architecture specifications visualizer
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = viewModel.t("auto_specs_title"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = viewModel.t("auto_specs_desc"),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { showDocDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer, contentColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(viewModel.t("generate_tz_readme"))
                    }
                }
            }
        }
    }

    if (showDocDialog) {
        AlertDialog(
            onDismissRequest = { showDocDialog = false },
            confirmButton = {
                TextButton(onClick = { showDocDialog = false }) {
                    Text(viewModel.t("great_btn"))
                }
            },
            title = {
                Text(text = viewModel.t("specs_dialog_title"))
            },
            text = {
                val proj = currentProj
                if (proj == null) {
                    Text(viewModel.t("specs_create_project_alert"))
                } else {
                    LazyColumn(modifier = Modifier.height(400.dp)) {
                        item {
                            Text(
                                text = "README.md - ${proj.name}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = """
                                    # ${proj.name}
                                    
                                    Авто-генерация выполнена посредством мобильной CMS-платформы MetaCMS.
                                    
                                    ## Конфигурация Стэка
                                    - Базовый язык: Dart 3.x, Flutter visual engine
                                    - PWA кэширование: Активно (интегрирован Service Worker)
                                    - Контейнеризация: В наличии Dockerfile конфигурации
                                    
                                    ## Активные Спецификации Модулей
                                    ${proj.activeModules.split(",").joinToString("\n") { "- [x] Модуль структуры: $it" }}
                                    
                                    ## Колонки и Свойства Entities СУБД (Custom Schema Fields)
                                    ${viewModel.getSchemaFields().mapIndexed { idx, field -> "${idx + 1}. Поле [${field.name}] типа [${field.type}] -> Описание: '${field.description}'" }.joinToString("\n")}
                                    """.trimIndent(),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = viewModel.t("deploy_instructions_title"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = """
                                1. Компиляция Flutter PWA (Web):
                                   `flutter build web --web-renderer canvaskit`
                                
                                2. Сборка Docker контейнера:
                                   `docker build -t ${proj.name.lowercase().replace(" ", "-")} -f docker/Dockerfile .`
                                   
                                3. Запуск веб-сервера локально:
                                   `docker run -p 8080:80 ${proj.name.lowercase().replace(" ", "-")}`
                                """.trimIndent(),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        )
    }
}

private fun getIconForModule(mod: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when(mod.trim().lowercase()) {
        "blog" -> Icons.Default.Book
        "users" -> Icons.Default.AccountBox
        "gallery" -> Icons.Default.Image
        "shop" -> Icons.Default.ShoppingCart
        "crm" -> Icons.Default.ContactPage
        "wiki" -> Icons.Default.Info
        "ai" -> Icons.Default.Psychology
        "analytics" -> Icons.Default.Star
        else -> Icons.Default.Extension
    }
}

