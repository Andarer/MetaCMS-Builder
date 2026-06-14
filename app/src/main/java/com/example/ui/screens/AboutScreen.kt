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
    val tabs = listOf("ТЗ & Доки", "Архитектура", "Roadmap & Logs")

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
                text = "Документация MetaCMS",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Изучите архитектурные стандарты, дорожную карту и инструкции к платформе",
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
                                    text = "Платформа MetaCMS Builder v1.0.0",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Русский / English Dual Engine",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Text(
                                    text = "MetaCMS Builder — это полноценная визуальная Android First среда для генерации современных масштабируемых CMS, CRM, Wiki, блогов, магазинов и решений искусственного интеллекта на базе единой кодовой базы.",
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )

                                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                                Text("Ключевые Особенности:", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                                BulletPoint("Android First: Управление, моделирование таблиц данных и экспорт прямо со смартфона.")
                                BulletPoint("GitHub First: Интеграция с GitHub Pages для полноценного веб-хостинга статических PWA систем.")
                                BulletPoint("AI First: Использование ИИ-ассистентов на базе Google Gemini 3.5 для авто-генерации структур БД.")
                                BulletPoint("Module First: Готовые модули Blog, CRM, Users, Gallery, Shop, Wiki, AI, Analytics в один клик.")
                            }
                        }
                    }

                    item {
                        Text("Инструкция по Offline PWA & Docker Сборкам:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
                            text = "Структура Архитектурных Модулей MetaCMS",
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
                                FolderElement("/app", "Корневая оболочка Android, координирующая визуальный билдер.")
                                FolderElement("/core", "Внутреннее ядро, сервисы авторизации, СУБД SQLite локального состояния.")
                                FolderElement("/builder", "ИИ транспиляторы, компиляторы Markdown спецификаций ТЗ и Dart файлов.")
                                FolderElement("/modules", "Код-пакеты (Blog, CRM, Shop, AI, Analytics, Wiki Engine).")
                                FolderElement("/docs", "Документация, README, CHANGELOG и спецификации систем.")
                                FolderElement("/docker", "Сценарии Docker-compose и Dockerfile тонкой настройки окружений.")
                                FolderElement("/.github", "Профайлы GitHub Actions сборников deploy.yml для Pages.")
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Диаграмма Потоков Данных Web PWA:",
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
                                text = "Android Visual DB Model (Room SQLite) --> Export Zip or Github Commits (PAT Tokens) --> Trigger Actions Compiling Node/Flutter Web --> Push gh-pages branch --> Live URL!",
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
                            text = "Дорожная Карта MetaCMS (ROADMAP)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    item {
                        Card {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("План Разработки проекта:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item1, onCheckedChange = { item1 = it })
                                    Text("v1.0.0: Сборка UI Билдера, Room архитектура, селф-хостинг", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item2, onCheckedChange = { item2 = it })
                                    Text("v1.1.0: Интеграция Gemini API для автоконфигурации схем СУБД", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item3, onCheckedChange = { item3 = it })
                                    Text("v1.2.0: Поддержка консоли Simulated Git: Commits & Pushes", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item4, onCheckedChange = { item4 = it })
                                    Text("v1.3.0: Живой интерактивный симулятор виртуальной БД (Sandbox Player)", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item5, onCheckedChange = { item5 = it })
                                    Text("v1.5.0: Локальная компиляция Flutter Web PWA", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = item6, onCheckedChange = { item6 = it })
                                    Text("v2.0.0 Global: Облачный экспорт баз данных в Spanner/Cloud SQL", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    item {
                        Text("История Изменений (CHANGELOG):", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("v1.3.0 Release (Текущий)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.tertiary)
                                BulletPoint("Интегрирована живая песочница баз данных (Database Simulation Engine).")
                                BulletPoint("Возможность вводить значения полей и генерировать записи таблиц динамически.")
                                BulletPoint("Добавлена поддержка интерактивного удаления и модификации мок-записей прямо из Android.")
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("v1.0.0 Core Build", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                BulletPoint("Интегрировано Room Database для сохранения проектов и модулей.")
                                BulletPoint("Добавлен живой визуализатор исходного кода Dart для 8 модулей.")
                                BulletPoint("Подключена модель ИИ Gemini 3.5 Flash с разбором и автоприменением JSON-Schema.")
                                BulletPoint("Спроектирован премиальный UI-дизайн Material 3 с Edge-To-Edge отображением.")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("•", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        Text(text = text, fontSize = 12.sp)
    }
}

@Composable
fun FolderElement(name: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
        Column {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
            Text(desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
