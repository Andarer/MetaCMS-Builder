package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.CmsViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: CmsViewModel = viewModel()
            val themeMode by viewModel.selectedThemeMode.collectAsState()
            val systemDark = androidx.compose.foundation.isSystemInDarkTheme()
            val computedDarkTheme = remember(themeMode, systemDark) {
                when (themeMode) {
                    "Day" -> false
                    "Night" -> true
                    "Scheduled" -> {
                        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
                        hour !in 6..19
                    }
                    else -> systemDark
                }
            }

            MyApplicationTheme(darkTheme = computedDarkTheme) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

                val activeProject by viewModel.currentProject.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val screenTitle = when (currentRoute) {
                            "dashboard" -> viewModel.t("screen_title_dashboard")
                            "projects" -> viewModel.t("screen_title_projects")
                            "modules" -> viewModel.t("screen_title_modules")
                            "assistant" -> viewModel.t("screen_title_assistant")
                            "github" -> viewModel.t("screen_title_github")
                            "settings" -> viewModel.t("screen_title_settings")
                            "about" -> viewModel.t("screen_title_about")
                            else -> "MetaCMS Builder"
                        }

                        CenterAlignedTopAppBar(
                            title = {
                                Column {
                                    Text(
                                        text = screenTitle,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    )
                                    activeProject?.let {
                                        Text(
                                            text = "${viewModel.t("active_project_label")}: ${it.name}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { navController.navigate("about") },
                                    modifier = Modifier.testTag("action_about")
                                ) {
                                    Icon(
                                        imageVector = if (currentRoute == "about") Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                                        contentDescription = viewModel.t("screen_title_about")
                                    )
                                }
                                IconButton(
                                    onClick = { navController.navigate("settings") },
                                    modifier = Modifier.testTag("action_settings")
                                ) {
                                    Icon(
                                        imageVector = if (currentRoute == "settings") Icons.Filled.Settings else Icons.Outlined.Settings,
                                        contentDescription = viewModel.t("screen_title_settings")
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    },
                    bottomBar = {
                        // Display bottom navigation bar for primary tabs only
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            val items = listOf(
                                NavigationBarNavItem(
                                    route = "dashboard",
                                    label = viewModel.t("nav_info"),
                                    selectedIcon = Icons.Filled.Dashboard,
                                    unselectedIcon = Icons.Outlined.Dashboard,
                                    tag = "nav_dashboard"
                                ),
                                NavigationBarNavItem(
                                    route = "projects",
                                    label = viewModel.t("nav_projects"),
                                    selectedIcon = Icons.Filled.FolderOpen,
                                    unselectedIcon = Icons.Outlined.Folder,
                                    tag = "nav_projects"
                                ),
                                NavigationBarNavItem(
                                    route = "modules",
                                    label = viewModel.t("nav_modules"),
                                    selectedIcon = Icons.Filled.Extension,
                                    unselectedIcon = Icons.Outlined.Extension,
                                    tag = "nav_modules"
                                ),
                                NavigationBarNavItem(
                                    route = "assistant",
                                    label = viewModel.t("nav_assistant"),
                                    selectedIcon = Icons.Filled.Psychology,
                                    unselectedIcon = Icons.Outlined.Psychology,
                                    tag = "nav_assistant"
                                ),
                                NavigationBarNavItem(
                                    route = "github",
                                    label = viewModel.t("nav_github"),
                                    selectedIcon = Icons.Filled.Code,
                                    unselectedIcon = Icons.Outlined.Code,
                                    tag = "nav_github"
                                )
                            )

                            items.forEach { item ->
                                val selected = currentRoute == item.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.label
                                        )
                                    },
                                    label = { Text(item.label, fontSize = 11.sp) },
                                    modifier = Modifier.testTag(item.tag)
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable("dashboard") {
                            DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToModules = { navController.navigate("modules") },
                                onNavigateToGithub = { navController.navigate("github") }
                            )
                        }
                        composable("projects") {
                            ProjectsScreen(viewModel = viewModel)
                        }
                        composable("modules") {
                            ModulesScreen(viewModel = viewModel)
                        }
                        composable("assistant") {
                            AssistantScreen(viewModel = viewModel)
                        }
                        composable("github") {
                            GithubScreen(viewModel = viewModel)
                        }
                        composable("settings") {
                            SettingsScreen(viewModel = viewModel)
                        }
                        composable("about") {
                            AboutScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

data class NavigationBarNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)
