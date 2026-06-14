package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.CmsViewModel
import com.example.viewmodel.SchemaField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModulesScreen(viewModel: CmsViewModel) {
    val currentProj by viewModel.currentProject.collectAsState()
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val allModulesList = listOf("Blog", "Users", "Gallery", "Shop", "CRM", "Wiki", "AI", "Analytics")
    var selectedModuleCodeName by remember { mutableStateOf(allModulesList[0]) }

    var isAddingField by remember { mutableStateOf(false) }
    var fieldName by remember { mutableStateOf("") }
    val fieldTypes = listOf("String", "Int", "Boolean", "Float")
    var selectedFieldType by remember { mutableStateOf(fieldTypes[0]) }
    var fieldDesc by remember { mutableStateOf("") }

    if (currentProj == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.Default.Source, 
                    contentDescription = null, 
                    modifier = Modifier.size(64.dp), 
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = viewModel.t("specs_create_project_alert"), 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    val activeModules = currentProj!!.activeModules.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    val schemaFields = viewModel.getSchemaFields()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("modules_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Toggle Module Selection Row
        item {
            Column {
                Text(
                    text = viewModel.t("modules_header"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = viewModel.t("modules_desc"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Horizontal picker active status
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = viewModel.t("active_modules_card_title"),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = viewModel.t("active_modules_card_desc"),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allModulesList.forEach { mod ->
                            val isActive = activeModules.contains(mod)
                            FilterChip(
                                selected = isActive,
                                onClick = { viewModel.toggleModule(mod) },
                                label = { Text(mod) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = getIconForModule(mod),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // Schema Column Modeler
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(viewModel.t("schema_editor_title"), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(viewModel.t("schema_editor_desc"), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        IconButton(onClick = { isAddingField = true }) {
                            Icon(Icons.Default.AddCircle, contentDescription = "Add Column", tint = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (schemaFields.isEmpty()) {
                        Text(
                            text = viewModel.t("no_schema_fields"),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            schemaFields.forEach { field ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Badge(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) {
                                            Text(
                                                text = field.type,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.padding(2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(text = field.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            if (field.description.isNotEmpty()) {
                                                Text(text = field.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.removeSchemaField(field) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.RemoveCircle,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Live Generated Code Tab Layout
        item {
            Column {
                Text(
                    text = "Архитектурный Код-Генератор",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Автогенерация классов сущностей и репозиториев для Flutter",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Select module code to preview
        item {
            ScrollableTabRow(
                selectedTabIndex = allModulesList.indexOf(selectedModuleCodeName),
                edgePadding = 0.dp
            ) {
                allModulesList.forEach { mod ->
                    val isEnabled = activeModules.contains(mod)
                    Tab(
                        selected = selectedModuleCodeName == mod,
                        onClick = { selectedModuleCodeName = mod },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(mod)
                                if (isEnabled) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }

        // High-fidelity Code viewer
        item {
            val generatedCode = viewModel.getDynamicCodeForModule(selectedModuleCodeName)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$selectedModuleCodeName.dart (Generated)",
                            color = Color(0xFF85C1E9),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = {
                                clipboard.setText(AnnotatedString(generatedCode))
                                Toast.makeText(context, "$selectedModuleCodeName source code copied!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = Color.LightGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val horizontalScrollState = rememberScrollState()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp)
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        Text(
                            text = generatedCode,
                            color = Color(0xFFD4D4D4),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // 4. Interactive Database Simulator Card (Visual Sandbox Prototype)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.12f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                var simModule by remember { mutableStateOf(allModulesList[0]) }
                val activeRecordsMap by viewModel.mockRecords.collectAsState()
                val currentRecords = activeRecordsMap[simModule] ?: emptyList()
                val specFields = viewModel.getSchemaFields()
                val pendingInputValues = remember { mutableStateMapOf<String, String>() }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = viewModel.t("sandbox_title"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                            Text(
                                text = viewModel.t("sandbox_desc"),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                            )
                        }

                        // Module Selection Menu
                        var expandedSelector by remember { mutableStateOf(false) }
                        Box {
                            TextButton(onClick = { expandedSelector = true }) {
                                Text(
                                    text = "$simModule ▾",
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            DropdownMenu(
                                expanded = expandedSelector,
                                onDismissRequest = { expandedSelector = false }
                            ) {
                                allModulesList.forEach { mod ->
                                    DropdownMenuItem(
                                        text = { Text(mod) },
                                        onClick = {
                                            simModule = mod
                                            expandedSelector = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!activeModules.contains(simModule)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = viewModel.t("sandbox_module_disabled") + " ($simModule)",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        if (specFields.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = viewModel.t("sandbox_no_fields"),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            Text(
                                text = "${viewModel.t("sandbox_add_record_title")} `$simModule`:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            specFields.forEach { field ->
                                val valState = pendingInputValues[field.name] ?: ""
                                OutlinedTextField(
                                    value = valState,
                                    onValueChange = { pendingInputValues[field.name] = it },
                                    label = { Text("${field.name} (${field.type})") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    val newRecord = mutableMapOf<String, String>()
                                    specFields.forEach { f ->
                                        newRecord[f.name] = pendingInputValues[f.name] ?: ""
                                    }
                                    viewModel.addMockRecord(simModule, newRecord)
                                    pendingInputValues.clear()
                                    Toast.makeText(context, "Stored live record in $simModule!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(viewModel.t("sandbox_btn"), fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "${viewModel.t("sandbox_db_table")} `$simModule` (${currentRecords.size}):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        if (currentRecords.isEmpty()) {
                            Text(
                                text = viewModel.t("sandbox_db_empty"),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.6f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                currentRecords.forEachIndexed { idx, record ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Запись ID #${currentRecords.size - idx}",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                IconButton(
                                                    onClick = { viewModel.deleteMockRecord(simModule, idx) },
                                                    modifier = Modifier.size(20.dp)
                                                ) {
                                                    Icon(
                                                        Icons.Default.Delete,
                                                        contentDescription = "Delete",
                                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Spacer(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))

                                            record.forEach { (k, v) ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 2.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = k,
                                                        fontFamily = FontFamily.Monospace,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Text(
                                                        text = v.ifBlank { "—" },
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Normal,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        textAlign = TextAlign.End
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal dialog to append a schema column manually
    if (isAddingField) {
        AlertDialog(
            onDismissRequest = { isAddingField = false },
            title = { Text(viewModel.t("dialog_add_field_h")) },
            confirmButton = {
                Button(
                    onClick = {
                        if (fieldName.isNotBlank()) {
                            viewModel.addSchemaField(
                                SchemaField(
                                    name = fieldName.trim(),
                                    type = selectedFieldType,
                                    description = fieldDesc.trim()
                                )
                            )
                            fieldName = ""
                            fieldDesc = ""
                            isAddingField = false
                        } else {
                            Toast.makeText(context, viewModel.t("toast_field_name_req"), Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(viewModel.t("btn_add"))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        fieldName = ""
                        fieldDesc = ""
                        isAddingField = false
                    }
                ) {
                    Text(viewModel.t("btn_cancel"))
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = fieldName,
                        onValueChange = { fieldName = it },
                        label = { Text(viewModel.t("field_name_l")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Column {
                        Text(
                            text = viewModel.t("field_type_l"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            fieldTypes.forEach { type ->
                                FilterChip(
                                    selected = selectedFieldType == type,
                                    onClick = { selectedFieldType = type },
                                    label = { Text(type) }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = fieldDesc,
                        onValueChange = { fieldDesc = it },
                        label = { Text(viewModel.t("field_desc_l")) },
                        modifier = Modifier.fillMaxWidth()
                    )
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

