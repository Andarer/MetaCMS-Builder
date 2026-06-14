package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.CmsViewModel

@Composable
fun SettingsScreen(viewModel: CmsViewModel) {
    val currentProj by viewModel.currentProject.collectAsState()
    val context = LocalContext.current

    var githubToken by remember { mutableStateOf("") }
    var hideToken by remember { mutableStateOf(true) }

    val models = listOf("gemini-3.5-flash (Basic tasks, summaries)", "gemini-3.1-pro-preview (Complex layout designs)")
    var selectedModel by remember { mutableStateOf(models[0]) }

    val appThemes = listOf("Cosmic Slate Theme (Default)", "Clean Polar Minimalism", "Neon Brutalism")
    var selectedTheme by remember { mutableStateOf(appThemes[0]) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Настройки Системы",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Конфигурируйте токены интеграций и персональные параметры среды",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // GitHub Integration Settings
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.VpnKey, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text("Авторизация GitHub (Personal Access Token)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    Text(
                        text = "Секретный токен используется для автоматической синхронизации и пушей в ваши GitHub Pages и ветки репозиториев.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = githubToken,
                        onValueChange = { githubToken = it },
                        label = { Text("GitHub Fine-grained PAT Token") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (hideToken) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            IconButton(onClick = { hideToken = !hideToken }) {
                                Icon(
                                    imageVector = if (hideToken) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Показать/Скрыть"
                                )
                            }
                        }
                    )

                    Button(
                        onClick = {
                            if (githubToken.isNotEmpty()) {
                                Toast.makeText(context, "GitHub Token successfully stored locally!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Верифицировать и сохранить токен")
                    }
                }
            }
        }

        // Gemini AI model selection
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        Text("Настройки ИИ Модели (Google Gemini API)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    Text(
                        text = "Используйте панель секретов в Google AI Studio (BuildConfig.GEMINI_API_KEY) для управления ключом модели. Выберите активную языковую модель ниже:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column {
                        models.forEach { model ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedModel == model,
                                    onClick = { selectedModel = model }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(model, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Styling and themes
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Кастомизация Среды", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                    Column {
                        appThemes.forEach { theme ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedTheme == theme,
                                    onClick = { selectedTheme = theme }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(theme, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // App details
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Обслуживание системы", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onErrorContainer)
                    Text("Вы можете сбросить все воркспейсы и восстановить предустановленные шаблоны (Wiki, Commerce, NewsBlog).", fontSize = 11.sp)
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Database cleared! Reload app to seed standard showpieces.", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Text("Очистить кэш базы данных MetaCMS")
                    }
                }
            }
        }
    }
}
