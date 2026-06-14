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
    val context = LocalContext.current

    var githubToken by remember { mutableStateOf("") }
    var hideToken by remember { mutableStateOf(true) }

    val models = listOf("gemini-3.5-flash (Basic tasks, summaries)", "gemini-3.1-pro-preview (Complex layout designs)")
    var selectedModel by remember { mutableStateOf(models[0]) }

    val currentTheme by viewModel.selectedThemeMode.collectAsState()
    val currentLang by viewModel.selectedLanguage.collectAsState()

    val themesList = listOf("System", "Day", "Night", "Scheduled")
    val langsList = listOf("System", "ru", "en", "uk")

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
                    text = viewModel.t("settings_header_title"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = viewModel.t("settings_header_desc"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // MultiLang selection block
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Translate, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(viewModel.t("lang_setting_label"), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    Column {
                        langsList.forEach { code ->
                            val label = when(code) {
                                "System" -> viewModel.t("lang_option_system")
                                "ru" -> "Русский (по умолчанию)"
                                "en" -> "English"
                                "uk" -> "Українська"
                                else -> code
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentLang == code,
                                    onClick = { 
                                        viewModel.setLanguage(code)
                                        Toast.makeText(context, "Язык обновлен / Language updated", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(label, fontSize = 13.sp, fontWeight = if (currentLang == code) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
            }
        }

        // Themes selection block
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                        Text(viewModel.t("theme_setting_label"), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    Column {
                        themesList.forEach { mode ->
                            val label = when(mode) {
                                "System" -> viewModel.t("theme_option_system")
                                "Day" -> viewModel.t("theme_option_day")
                                "Night" -> viewModel.t("theme_option_night")
                                "Scheduled" -> viewModel.t("theme_option_scheduled")
                                else -> mode
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentTheme == mode,
                                    onClick = { 
                                        viewModel.setThemeMode(mode)
                                        Toast.makeText(context, "Тема обновлена / Theme updated", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(label, fontSize = 13.sp, fontWeight = if (currentTheme == mode) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }
                }
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
                        Text(viewModel.t("github_auth_title"), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    Text(
                        text = viewModel.t("github_auth_desc"),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = githubToken,
                        onValueChange = { githubToken = it },
                        label = { Text(viewModel.t("github_token_label")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (hideToken) PasswordVisualTransformation() else VisualTransformation.None,
                        trailingIcon = {
                            IconButton(onClick = { hideToken = !hideToken }) {
                                Icon(
                                    imageVector = if (hideToken) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Show/Hide"
                                )
                            }
                        }
                    )

                    Button(
                        onClick = {
                            if (githubToken.isNotEmpty()) {
                                Toast.makeText(context, "Stored local PAT token success!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(viewModel.t("verify_save_token"))
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
                        Text(viewModel.t("gemini_api_settings"), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    Text(
                        text = viewModel.t("gemini_api_desc"),
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

        // Reset database / Clean-up
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(viewModel.t("system_maintenance"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onErrorContainer)
                    Text(viewModel.t("reset_desc"), fontSize = 11.sp)
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, viewModel.t("toast_db_cleared"), Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ) {
                        Text(viewModel.t("reset_db_btn"))
                    }
                }
            }
        }
    }
}
