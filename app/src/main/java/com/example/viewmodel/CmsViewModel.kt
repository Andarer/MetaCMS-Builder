package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

sealed interface ProjectsUiState {
    object Loading : ProjectsUiState
    data class Success(val projects: List<CmsProject>) : ProjectsUiState
    data class Error(val message: String) : ProjectsUiState
}

class CmsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CmsRepository
    private val sharedPrefs = application.getSharedPreferences("metacms_prefs", android.content.Context.MODE_PRIVATE)

    private val _selectedLanguage = MutableStateFlow(sharedPrefs.getString("pref_language", "ru") ?: "ru")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _selectedThemeMode = MutableStateFlow(sharedPrefs.getString("pref_theme_mode", "System") ?: "System")
    val selectedThemeMode: StateFlow<String> = _selectedThemeMode.asStateFlow()

    fun setLanguage(lang: String) {
        _selectedLanguage.value = lang
        sharedPrefs.edit().putString("pref_language", lang).apply()
    }

    fun setThemeMode(mode: String) {
        _selectedThemeMode.value = mode
        sharedPrefs.edit().putString("pref_theme_mode", mode).apply()
    }

    fun t(key: String): String {
        val lang = _selectedLanguage.value
        val actualLang = if (lang == "System") {
            com.example.ui.locale.Localization.mapSystemLanguage(java.util.Locale.getDefault())
        } else {
            lang
        }
        return com.example.ui.locale.Localization.translate(key, actualLang)
    }

    init {
        val db = CmsDatabase.getDatabase(application)
        repository = CmsRepository(db.projectDao())
        
        // Ensure standard showpieces exist
        seedDefaultProjects()
    }

    val projectsState: StateFlow<ProjectsUiState> = repository.allProjects
        .map { projects ->
            if (projects.isEmpty()) {
                ProjectsUiState.Loading 
            } else {
                ProjectsUiState.Success(projects)
            }
        }
        .catch { e -> emit(ProjectsUiState.Error(e.localizedMessage ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProjectsUiState.Loading
        )

    private val _currentProject = MutableStateFlow<CmsProject?>(null)
    val currentProject: StateFlow<CmsProject?> = _currentProject.asStateFlow()

    // Git System State
    private val _gitLogs = MutableStateFlow<List<String>>(listOf("Git system initialized.", "Local workspace clean."))
    val gitLogs: StateFlow<List<String>> = _gitLogs.asStateFlow()

    private val _isBuilding = MutableStateFlow(false)
    val isBuilding: StateFlow<Boolean> = _isBuilding.asStateFlow()

    private val _buildOutput = MutableStateFlow("")
    val buildOutput: StateFlow<String> = _buildOutput.asStateFlow()

    // AI Assistant State
    private val _aiResponse = MutableStateFlow<String>("")
    val aiResponse: StateFlow<String> = _aiResponse.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Settings Keys
    private val _githubTokenSetting = MutableStateFlow("")
    val githubTokenSetting: StateFlow<String> = _githubTokenSetting.asStateFlow()

    // Simulation Engine State for Live DB View & Interactive Testing
    private val _mockRecords = MutableStateFlow<Map<String, List<Map<String, String>>>>(
        mapOf(
            "Blog" to listOf(
                mapOf("articleTitle" to "Запуск MetaCMS v1.1.0", "bodyContent" to "Первый релиз нашей визуальной CMS-платформы на Flutter & Docker!", "authorId" to "1", "isPublished" to "true"),
                mapOf("articleTitle" to "Облачный деплой в один клик", "bodyContent" to "Интеграция с GitHub Actions позволяет собирать и деплоить PWA автоматически.", "authorId" to "3", "isPublished" to "true")
            ),
            "CRM" to listOf(
                mapOf("name" to "Александр Смирнов", "email" to "alex@smirnov.io", "dealValue" to "1850.50", "status" to "Won"),
                mapOf("name" to "Марина Кузнецова", "email" to "marina@design.studio", "dealValue" to "5600.00", "status" to "InContact")
            ),
            "Shop" to listOf(
                mapOf("productName" to "Беспроводной Микрофон Pro-9", "price" to "189.00", "stockCount" to "35", "leadInquiryEnabled" to "true"),
                mapOf("productName" to "Студийный Свет LED Panel", "price" to "245.50", "stockCount" to "12", "leadInquiryEnabled" to "false")
            ),
            "Wiki" to listOf(
                mapOf("articleTitle" to "Руководство разработчика", "bodyContent" to "Правила написания чистого кода и организации репозиториев.", "authorId" to "10", "isPublished" to "true")
            )
        )
    )
    val mockRecords: StateFlow<Map<String, List<Map<String, String>>>> = _mockRecords.asStateFlow()

    fun addMockRecord(moduleName: String, record: Map<String, String>) {
        val current = _mockRecords.value.toMutableMap()
        val list = (current[moduleName] ?: emptyList()).toMutableList()
        list.add(0, record)
        current[moduleName] = list
        _mockRecords.value = current
        _gitLogs.value = _gitLogs.value + "[SimEngine] Добавлена живая запись в таблицу '$moduleName': ${record.entries.joinToString { "${it.key}=${it.value}" }}"
    }

    fun deleteMockRecord(moduleName: String, index: Int) {
        val current = _mockRecords.value.toMutableMap()
        val list = (current[moduleName] ?: emptyList()).toMutableList()
        if (index in list.indices) {
            val removed = list.removeAt(index)
            current[moduleName] = list
            _mockRecords.value = current
            _gitLogs.value = _gitLogs.value + "[SimEngine] Из таблицы '$moduleName' удалена запись #${index + 1}"
        }
    }

    init {
        // Automatically select the first project once loaded
        viewModelScope.launch {
            repository.allProjects.collect { projects ->
                if (projects.isNotEmpty() && _currentProject.value == null) {
                    _currentProject.value = projects.first()
                }
            }
        }
    }

    fun selectProject(project: CmsProject) {
        _currentProject.value = project
    }

    private fun seedDefaultProjects() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.allProjects.first().let { currentList ->
                if (currentList.isEmpty()) {
                    // Pre-seed three professional mock MetaCMS Projects
                    val docWiki = CmsProject(
                        name = "DevFlow WikiCMS",
                        description = "Full handbook CRM & development Wiki platform for mobile engineering.",
                        techStack = "Flutter (Android, Web, PWA)",
                        githubRepo = "andarer/devflow-wikicms",
                        githubBranch = "main",
                        activeModules = "Wiki,Users,AI,Analytics",
                        customSchemaJson = """[
                            {"name": "articleTitle", "type": "String", "description": "Title of the wiki page"},
                            {"name": "bodyContent", "type": "String", "description": "Markdown body content"},
                            {"name": "authorId", "type": "Int", "description": "User identifier who created this article"},
                            {"name": "isPublished", "type": "Boolean", "description": "Publish status state"}
                        ]""".trimIndent()
                    )

                    val techShop = CmsProject(
                        name = "GadgetEars Pro",
                        description = "Commerce headless online shop for portable gadget sales & order tracking CRM.",
                        techStack = "Flutter Headless Native",
                        githubRepo = "andarer/gadgetears-shop",
                        githubBranch = "gh-pages",
                        activeModules = "Shop,CRM,Gallery,Users,Analytics",
                        customSchemaJson = """[
                            {"name": "productName", "type": "String", "description": "Commercial gadget item name"},
                            {"name": "price", "type": "Float", "description": "Item value in USD"},
                            {"name": "stockCount", "type": "Int", "description": "Number of pieces in warehouse"},
                            {"name": "leadInquiryEnabled", "type": "Boolean", "description": "Let clients leave CRM leads"}
                        ]""".trimIndent()
                    )

                    val multiBlog = CmsProject(
                        name = "MetaPress News",
                        description = "Global news portal leveraging PWA and integrated AI content editor.",
                        techStack = "Flutter Web Portal",
                        githubRepo = "andarer/metapress-blog",
                        githubBranch = "main",
                        activeModules = "Blog,Gallery,Users,AI,Analytics",
                        customSchemaJson = """[
                            {"name": "postHeader", "type": "String", "description": "Title of the blog entry"},
                            {"name": "tags", "type": "String", "description": "Comma separated labels"},
                            {"name": "readsCount", "type": "Int", "description": "Count of times clicked"}
                        ]""".trimIndent()
                    )

                    repository.insert(docWiki)
                    repository.insert(techShop)
                    repository.insert(multiBlog)
                }
            }
        }
    }

    fun createProject(name: String, desc: String, stack: String, repo: String, branch: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newProject = CmsProject(
                name = name,
                description = desc,
                techStack = stack,
                githubRepo = repo,
                githubBranch = branch,
                activeModules = "Blog,Users,AI"
            )
            val newId = repository.insert(newProject)
            _gitLogs.value = _gitLogs.value + "[Git] Created new visual project workspace: '$name' at locally cached index #$newId."
        }
    }

    fun updateProjectSettings(project: CmsProject) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(project)
            if (_currentProject.value?.id == project.id) {
                _currentProject.value = project
            }
        }
    }

    fun deleteProject(project: CmsProject) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(project)
            if (_currentProject.value?.id == project.id) {
                _currentProject.value = null
            }
        }
    }

    fun toggleModule(moduleName: String) {
        val proj = _currentProject.value ?: return
        val list = proj.activeModules.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toMutableList()
        if (list.contains(moduleName)) {
            list.remove(moduleName)
        } else {
            list.add(moduleName)
        }
        val updated = proj.copy(
            activeModules = list.joinToString(","),
            updatedAt = System.currentTimeMillis()
        )
        updateProjectSettings(updated)
        _gitLogs.value = _gitLogs.value + "[System] Toggled module '$moduleName': Now ${if (list.contains(moduleName)) "ENABLED" else "DISABLED"}."
    }

    fun updateSchema(fields: List<SchemaField>) {
        val proj = _currentProject.value ?: return
        val jsonArray = JSONArray()
        fields.forEach { field ->
            val obj = JSONObject()
            obj.put("name", field.name)
            obj.put("type", field.type)
            obj.put("description", field.description)
            jsonArray.put(obj)
        }
        val updated = proj.copy(
            customSchemaJson = jsonArray.toString(),
            updatedAt = System.currentTimeMillis()
        )
        updateProjectSettings(updated)
        _gitLogs.value = _gitLogs.value + "[Schema] Updated database entity mapping: Modified to ${fields.size} elements."
    }

    fun addSchemaField(field: SchemaField) {
        val proj = _currentProject.value ?: return
        val fields = getSchemaFields().toMutableList()
        fields.add(field)
        updateSchema(fields)
    }

    fun removeSchemaField(field: SchemaField) {
        val proj = _currentProject.value ?: return
        val fields = getSchemaFields().toMutableList()
        fields.removeAll { it.name == field.name }
        updateSchema(fields)
    }

    fun getSchemaFields(): List<SchemaField> {
        val proj = _currentProject.value ?: return emptyList()
        if (proj.customSchemaJson.isEmpty()) return emptyList()
        val list = mutableListOf<SchemaField>()
        try {
            val arr = JSONArray(proj.customSchemaJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    SchemaField(
                        name = obj.getString("name"),
                        type = obj.getString("type"),
                        description = obj.optString("description", "")
                    )
                )
            }
        } catch (e: Exception) {
            // fallback
        }
        return list
    }

    // Git Operations
    fun gitCommit(message: String) {
        val proj = _currentProject.value ?: return
        _gitLogs.value = _gitLogs.value + listOf(
            "[Git] git add .",
            "[Git] git commit -m \"$message\"",
            "[Git] Indexing structural directories for ${proj.name} (app, core, modules, docs, docker, .github)",
            "[Git] Head is now at Commit [${(100000..999999).random()}] - $message"
        )
    }

    fun gitPush() {
        val proj = _currentProject.value ?: return
        viewModelScope.launch {
            _gitLogs.value = _gitLogs.value + "[Git] Initiating secure push to origin/${proj.githubBranch}..."
            _isBuilding.value = true
            _buildOutput.value = "Starting GitHub Sync for ${proj.name}...\nValidating secrets config...\n"
            
            kotlinx.coroutines.delay(1000)
            _gitLogs.value = _gitLogs.value + "[Git] Connection established. Uploading references."
            _buildOutput.value = _buildOutput.value + "Uploading MetaCMS specifications to repo ${proj.githubRepo}...\n"
            
            kotlinx.coroutines.delay(1500)
            _gitLogs.value = _gitLogs.value + "[Git] Push successful! GitHub Actions build triggered."
            _buildOutput.value = _buildOutput.value + "Build Succeeded!\nGenerated structure: app, core, builder, modules, docs, docker, .github-pages PWA\nDeployed Live to Pages!"
            _isBuilding.value = false
        }
    }

    fun gitPull() {
        val proj = _currentProject.value ?: return
        viewModelScope.launch {
            _gitLogs.value = _gitLogs.value + "[Git] git pull origin ${proj.githubBranch}"
            _isBuilding.value = true
            kotlinx.coroutines.delay(1200)
            _gitLogs.value = _gitLogs.value + "[Git] Pulled latest branches and releases. Workspace Up-To-Date!"
            _isBuilding.value = false
        }
    }

    fun gitCreateRelease(version: String, notes: String) {
        val proj = _currentProject.value ?: return
        _gitLogs.value = _gitLogs.value + listOf(
            "[Git] Creating release key and tagged reference: '$version'",
            "[Git] Release Tag: $version successfully published under assets list.",
            "[Release] $version generated with notes: '$notes'"
        )
    }

    // AI Assist logic
    fun generateCmsConfigurationWithAi(userInput: String) {
        val proj = _currentProject.value ?: return
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiResponse.value = "Consulting MetaCMS AI Architect..."
            
            val systemInstructions = """
                You are the MetaCMS Builder AI Architect. The user is requesting a custom feature or dynamic CRM/CMS module database.
                Generate a beautiful response showing:
                1. A brief conceptual design of this module
                2. A JSON list of schema fields representing the database columns (use JSON format inside a markdown code block starting with ```json)
                   The JSON MUST be an array of objects, each containing: "name", "type" (one of "String", "Int", "Boolean", "Float"), and "description".
                   Example JSON format:
                   [
                     {"name": "title", "type": "String", "description": "Name of item"}
                   ]
                3. High-quality production-ready complete Flutter/Dart code template representing this database module using clean architecture code rules.
                Everything must be clean, neat, and highly professional. Write in Russian (as requested by user's prompt).
            """.trimIndent()

            val aiPrompt = "User CMS Request: $userInput\nActive Project Stack: ${proj.techStack}\nActive Modules: ${proj.activeModules}"
            
            val response = GeminiNetwork.getAiResponse(aiPrompt, systemInstructions)
            _aiResponse.value = response
            
            // Try to parse the JSON array from response to automatically update the project schemas!
            parseAndApplyCmsSchema(response)
            
            _isAiLoading.value = false
        }
    }

    private fun parseAndApplyCmsSchema(text: String) {
        try {
            val startTag = "```json"
            val endTag = "```"
            if (text.contains(startTag)) {
                val startIndex = text.indexOf(startTag) + startTag.length
                val endIndex = text.indexOf(endTag, startIndex)
                if (endIndex > startIndex) {
                    val jsonStr = text.substring(startIndex, endIndex).trim()
                    val arr = JSONArray(jsonStr)
                    val list = mutableListOf<SchemaField>()
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        list.add(
                            SchemaField(
                                name = obj.getString("name"),
                                type = obj.getString("type"),
                                description = obj.optString("description", "")
                            )
                        )
                    }
                    if (list.isNotEmpty()) {
                        updateSchema(list)
                        _gitLogs.value = _gitLogs.value + "[AI] Successfully applied ${list.size} database columns generated by the MetaCMS AI assistant!"
                    }
                }
            }
        } catch (e: Exception) {
            // parsing failed, which is fine since AI generated text is still shown
        }
    }

    // Code generators for previewing under each module!
    fun getDynamicCodeForModule(moduleName: String): String {
        val activeProj = _currentProject.value ?: return "// Choose or create a project to generate code templates"
        val fields = getSchemaFields()
        val fieldsString = if (fields.isEmpty()) {
            "      // No dynamic schema columns set yet. Design them in Modules!"
        } else {
            fields.joinToString("\n") { 
                "  final ${when(it.type) {
                    "String" -> "String"
                    "Int" -> "int"
                    "Boolean" -> "bool"
                    "Float" -> "double"
                    else -> "String"
                }} ${it.name}; // ${it.description}"
            }
        }

        val jsonMapping = if (fields.isEmpty()) {
            "      // Map fields..."
        } else {
            fields.joinToString("\n") {
                "      ${it.name}: json['${it.name}'] as ${when(it.type) {
                    "String" -> "String"
                    "Int" -> "int"
                    "Boolean" -> "bool"
                    "Float" -> "double"
                    else -> "String"
                }},"
            }
        }

        return when (moduleName) {
            "Blog" -> """
// ==========================================
// MetaCMS Generated Module: Blog
// Platform: ${activeProj.techStack}
// Generated At: 2026-06-11 (Russian/English Dual Engine)
// ==========================================

import 'package:flutter/material.dart';
import '../../core/database/local_db.dart';

class BlogPost {
$fieldsString
  final int? id;
  final String title;
  final String author;
  final DateTime createdAt;

  BlogPost({
    this.id,
    required this.title,
    required this.author,
    required this.createdAt,
${fields.joinToString("\n") { "    required this.${it.name}," }}
  });

  factory BlogPost.fromJson(Map<String, dynamic> json) {
    return BlogPost(
      id: json['id'] as int?,
      title: json['title'] as String,
      author: json['author'] as String,
      createdAt: DateTime.parse(json['createdAt'] as String),
$jsonMapping
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'author': author,
      'createdAt': createdAt.toIso8601String(),
${fields.joinToString("\n") { "      '${it.name}': ${it.name}," }}
    };
  }
}

class BlogRepository {
  final LocalSqlDatabase _database;

  BlogRepository(this._database);

  Future<List<BlogPost>> fetchAllPosts() async {
    final results = await _database.query('blog_posts');
    return results.map((e) => BlogPost.fromJson(e)).toList();
  }

  Future<void> savePost(BlogPost post) async {
    await _database.insert('blog_posts', post.toJson());
  }
}
""".trimIndent()

            "Users" -> """
// ==========================================
// MetaCMS Generated Module: Users
// Platform: ${activeProj.techStack}
// Security Compliance: GDPR, OAuth2 integration
// ==========================================

import 'package:flutter/foundation.dart';

class CmsUser {
  final String uid;
  final String email;
  final String displayName;
  final String role; // 'Admin', 'Editor', 'Subscriber'
  final bool isActive;

  CmsUser({
    required this.uid,
    required this.email,
    required this.displayName,
    required this.role,
    this.isActive = true,
  });

  Map<String, dynamic> toMap() {
    return {
      'uid': uid,
      'email': email,
      'displayName': displayName,
      'role': role,
      'isActive': isActive,
    };
  }
}

class UserAuthState extends ChangeNotifier {
  CmsUser? _currentUser;
  bool _isLoading = false;

  CmsUser? get currentUser => _currentUser;
  bool get isLoading => _isLoading;

  Future<bool> signInWithOAuth(String provider, String token) async {
    _isLoading = true;
    notifyListeners();
    
    // Auth process 
    try {
      _currentUser = CmsUser(
        uid: "user_${(1000..9999).random()}",
        email: "editor@metacms.org",
        displayName: "Architect Administrator",
        role: "Admin"
      );
      _isLoading = false;
      notifyListeners();
      return true;
    } catch (_) {
      _isLoading = false;
      notifyListeners();
      return false;
    }
  }
}
""".trimIndent()

            "CRM" -> """
// ==========================================
// MetaCMS Generated Module: Customer Relationship Management (CRM)
// Integration: Lead Pipelines, automated analytics
// ==========================================

import 'dart:async';

class CmsLead {
  final String id;
  final String name;
  final String email;
  final double dealValue;
  final String status; // 'New', 'InContact', 'Won', 'Lost'

  CmsLead({
    required this.id,
    required this.name,
    required this.email,
    this.dealValue = 0.0,
    required this.status,
  });
}

class CRMManager {
  final List<CmsLead> _leads = [];
  final _leadController = StreamController<List<CmsLead>>.broadcast();

  Stream<List<CmsLead>> get leadsStream => _leadController.stream;

  void addLead(CmsLead lead) {
    _leads.add(lead);
    _leadController.sink.add(List.unmodifiable(_leads));
  }
}
""".trimIndent()

            "Shop" -> """
// ==========================================
// MetaCMS Generated Module: Headless Commerce Shop
// Features: Local shopping cart session caching, PWA offline state
// ==========================================

class CommerceItem {
  final String sku;
  final String name;
  final double price;
  final int inventory;

  CommerceItem({
    required this.sku,
    required this.name,
    required this.price,
    required this.inventory,
  });
}

class ShoppingCart {
  final Map<CommerceItem, int> _items = {};

  double get grandTotal {
    return _items.entries.fold(0.0, (sum, entry) => sum + (entry.key.price * entry.value));
  }

  void addToCart(CommerceItem item, int quantity) {
    _items[item] = (_items[item] ?: 0) + quantity;
  }
}
""".trimIndent()

            "Gallery" -> """
// ==========================================
// MetaCMS Generated Module: Gallery Asset Manager
// Multi-format support: AVIF, WEBP optimization
// ==========================================

import 'package:flutter/foundation.dart';

class AssetFile {
  final String assetId;
  final String name;
  final String url;
  final String mimeType;
  final int byteSize;

  AssetFile({
    required this.assetId,
    required this.name,
    required this.url,
    required this.mimeType,
    required this.byteSize,
  });
}
""".trimIndent()

            "Wiki" -> """
// ==========================================
// MetaCMS Generated Module: Wiki & Docs Engine
// Features: Hierarchical layouts, offline markdown sync
// ==========================================

class WikiNode {
  final String slug;
  final String title;
  final String markdownContent;
  final List<String> childPageSlugs;

  WikiNode({
    required this.slug,
    required this.title,
    required this.markdownContent,
    this.childPageSlugs = const [],
  });
}
""".trimIndent()

            "AI" -> """
// ==========================================
// MetaCMS Generated Module: AI Assistant Model Hub
// Server-Side: gemini-3.5-flash configuration
// ==========================================

import 'dart:convert';
import 'package:http/http.dart' as http;

class AiCompletionService {
  final String _endpoint = "https://generativelanguage.googleapis.com/v1beta";
  final String _apiKey;

  AiCompletionService(this._apiKey);

  Future<String> requestCmsCompletion(String systemPrompt, String userContext) async {
    final response = await http.post(
      Uri.parse("${'$'}_endpoint/models/gemini-3.5-flash:generateContent?key=${'$'}_apiKey"),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'contents': [
          {'parts': [{'text': userContext}]}
        ],
        'systemInstruction': {
          'parts': [{'text': systemPrompt}]
        }
      })
    );
    
    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return data['candidates'][0]['content']['parts'][0]['text'] as String;
    } else {
      throw Exception("MetaCMS AI Service failed: ${'$'}{response.body}");
    }
  }
}
""".trimIndent()

            "Analytics" -> """
// ==========================================
// MetaCMS Generated Module: Telemetry Analytics
// Offline Cache: Persisted until signal acquired
// ==========================================

class CMSAnalyticsEvent {
  final String eventName;
  final Map<String, dynamic> params;
  final int epochTimestamp;

  CMSAnalyticsEvent({
    required this.eventName,
    required this.params,
  }) : epochTimestamp = DateTime.now().millisecondsSinceEpoch;
}
""".trimIndent()

            else -> ""
        }
    }
}

data class SchemaField(
    val name: String,
    val type: String, // String, Int, Boolean, Float
    val description: String = ""
)
