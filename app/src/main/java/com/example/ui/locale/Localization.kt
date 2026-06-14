package com.example.ui.locale

import java.util.Locale

object Localization {
    private val translations = mapOf(
        "nav_info" to mapOf(
            "ru" to "Инфо",
            "en" to "Info",
            "uk" to "Інфо"
        ),
        "nav_projects" to mapOf(
            "ru" to "Проекты",
            "en" to "Projects",
            "uk" to "Проекти"
        ),
        "nav_modules" to mapOf(
            "ru" to "Схема",
            "en" to "Schema",
            "uk" to "Схема"
        ),
        "nav_assistant" to mapOf(
            "ru" to "ИИ",
            "en" to "AI",
            "uk" to "ШІ"
        ),
        "nav_github" to mapOf(
            "ru" to "Git",
            "en" to "Git",
            "uk" to "Git"
        ),
        "screen_title_dashboard" to mapOf(
            "ru" to "Панель MetaCMS",
            "en" to "MetaCMS Dashboard",
            "uk" to "Панель MetaCMS"
        ),
        "screen_title_projects" to mapOf(
            "ru" to "Воркспейсы",
            "en" to "Workspaces",
            "uk" to "Воркспейси"
        ),
        "screen_title_modules" to mapOf(
            "ru" to "Конфигуратор схем",
            "en" to "Schema Configurator",
            "uk" to "Конфігуратор схем"
        ),
        "screen_title_assistant" to mapOf(
            "ru" to "ИИ Архитектор",
            "en" to "AI Architect",
            "uk" to "ШІ Архітектор"
        ),
        "screen_title_github" to mapOf(
            "ru" to "Git Сборщик",
            "en" to "Git Builder",
            "uk" to "Git Збирач"
        ),
        "screen_title_settings" to mapOf(
            "ru" to "Параметры",
            "en" to "Parameters",
            "uk" to "Параметри"
        ),
        "screen_title_about" to mapOf(
            "ru" to "Релиз & ТЗ",
            "en" to "Release & Spec",
            "uk" to "Реліз & ТЗ"
        ),
        "active_project_label" to mapOf(
            "ru" to "Проект",
            "en" to "Project",
            "uk" to "Проєкт"
        ),
        "metacms_subtitle" to mapOf(
            "ru" to "Разрабатывайте CMS, CRM и Wiki системы на лету",
            "en" to "Develop CMS, CRM and Wiki systems on the fly",
            "uk" to "Розробляйте CMS, CRM та Wiki системи на льоту"
        ),
        "selected_project_title" to mapOf(
            "ru" to "Выбранный Проект",
            "en" to "Selected Project",
            "uk" to "Вибраний Проєкт"
        ),
        "none_project" to mapOf(
            "ru" to "Нет активного проекта",
            "en" to "No active project",
            "uk" to "Немає активного проєкту"
        ),
        "projects_empty" to mapOf(
            "ru" to "Проекты не созданы",
            "en" to "No workspaces created",
            "uk" to "Проєкти не створені"
        ),
        "projects_empty_desc" to mapOf(
            "ru" to "Перейдите на вкладку проектов, чтобы добавить новый.",
            "en" to "Go to the projects tab to add a new one.",
            "uk" to "Перейдіть на вкладку проєктів, щоб додати новий."
        ),
        "system_metrics" to mapOf(
            "ru" to "Показатели Системы",
            "en" to "System Metrics",
            "uk" to "Показники Системи"
        ),
        "active_modules" to mapOf(
            "ru" to "Активных Модулей",
            "en" to "Active Modules",
            "uk" to "Активних Модулів"
        ),
        "database_fields" to mapOf(
            "ru" to "Полей Схемы Базы",
            "en" to "Db Schema Fields",
            "uk" to "Полів Схеми Бази"
        ),
        "github_status_title" to mapOf(
            "ru" to "Статус GitHub Pages / Actions",
            "en" to "GitHub Pages / Actions Status",
            "uk" to "Статус GitHub Pages / Actions"
        ),
        "compilation_export" to mapOf(
            "ru" to "Компиляция и экспорт...",
            "en" to "Compilation & export...",
            "uk" to "Компіляція та експорт..."
        ),
        "deployed_successfully" to mapOf(
            "ru" to "Успешно развернут",
            "en" to "Deployed successfully",
            "uk" to "Успішно розгорнуто"
        ),
        "hosts" to mapOf(
            "ru" to "Хост",
            "en" to "Host",
            "uk" to "Хост"
        ),
        "git_console_btn" to mapOf(
            "ru" to "Git Консоль",
            "en" to "Git Console",
            "uk" to "Git Консоль"
        ),
        "documents_btn" to mapOf(
            "ru" to "Документы",
            "en" to "Documents",
            "uk" to "Документи"
        ),
        "auto_specs_title" to mapOf(
            "ru" to "Автогенерация Спецификаций",
            "en" to "Auto Specs Generation",
            "uk" to "Автогенерація Специфікацій"
        ),
        "auto_specs_desc" to mapOf(
            "ru" to "Скомпилируйте файлы ТЗ, ROADMAP и README на основе выбранных модулей проекта.",
            "en" to "Compile Spec, Roadmap, and Readme templates matching your project modules.",
            "uk" to "Скомпілюйте файли ТЗ, ROADMAP та README на основі вибраних модулів проєкту."
        ),
        "generate_tz_readme" to mapOf(
            "ru" to "Сгенерировать ТЗ и README",
            "en" to "Generate Spec & README",
            "uk" to "Згенерувати ТЗ та README"
        ),
        "specs_dialog_title" to mapOf(
            "ru" to "MetaCMS Архитектура & ТЗ",
            "en" to "MetaCMS Architecture & Spec",
            "uk" to "MetaCMS Архітектура & ТЗ"
        ),
        "specs_create_project_alert" to mapOf(
            "ru" to "Создайте проект для просмотра ТЗ.",
            "en" to "Create a project to view the design spec.",
            "uk" to "Створіть проєкт для перегляду ТЗ."
        ),
        "deploy_instructions_title" to mapOf(
            "ru" to "Инструкция по развертыванию Docker & PWA",
            "en" to "Deploy Instruction: Docker & PWA",
            "uk" to "Інструкція з розгортання Docker & PWA"
        ),
        "great_btn" to mapOf(
            "ru" to "Отлично",
            "en" to "Great",
            "uk" to "Чудово"
        ),
        
        // Settings Screen
        "settings_header_title" to mapOf(
            "ru" to "Настройки Системы",
            "en" to "System Settings",
            "uk" to "Налаштування Системи"
        ),
        "settings_header_desc" to mapOf(
            "ru" to "Конфигурируйте токены интеграций и персональные параметры среды",
            "en" to "Configure integration tokens and custom environment settings",
            "uk" to "Конфігуруйте токени інтеграцій та персональні параметри середовища"
        ),
        "github_auth_title" to mapOf(
            "ru" to "Авторизация GitHub (Personal Access Token)",
            "en" to "GitHub Auth (Personal Access Token)",
            "uk" to "Авторизація GitHub (Personal Access Token)"
        ),
        "github_auth_desc" to mapOf(
            "ru" to "Секретный токен используется для автоматической синхронизации и пушей в ваши GitHub Pages и ветки репозиториев.",
            "en" to "The secret token is used to auto-synchronize build pushes to your GitHub Pages branches.",
            "uk" to "Секретний токен використовується для автоматичної синхронізації та пушів у ваші GitHub Pages та гілки репозиторіїв."
        ),
        "github_token_label" to mapOf(
            "ru" to "GitHub Fine-grained PAT Токен",
            "en" to "GitHub Fine-grained PAT Token",
            "uk" to "GitHub Fine-grained PAT Токен"
        ),
        "verify_save_token" to mapOf(
            "ru" to "Верифицировать и сохранить токен",
            "en" to "Verify and Store Token",
            "uk" to "Верифікувати та зберегти токен"
        ),
        "gemini_api_settings" to mapOf(
            "ru" to "Настройки ИИ Модели (Google Gemini API)",
            "en" to "AI Model Settings (Google Gemini API)",
            "uk" to "Налаштування моделі ШІ (Google Gemini API)"
        ),
        "gemini_api_desc" to mapOf(
            "ru" to "Используйте панель секретов в Google AI Studio (BuildConfig.GEMINI_API_KEY) для управления ключом модели. Выберите активную языковую модель ниже:",
            "en" to "Manage keys via the Secrets panel in Google AI Studio (BuildConfig.GEMINI_API_KEY). Choose active model below:",
            "uk" to "Керуйте ключами через панель секретів у Google AI Studio (BuildConfig.GEMINI_API_KEY). Оберіть активну модель нижче:"
        ),
        "env_customization" to mapOf(
            "ru" to "Кастомизация Среды",
            "en" to "Environment Customization",
            "uk" to "Кастомізація Середовища"
        ),
        "system_maintenance" to mapOf(
            "ru" to "Обслуживание Системы",
            "en" to "System Maintenance",
            "uk" to "Обслуговування Системи"
        ),
        "reset_desc" to mapOf(
            "ru" to "Вы можете сбросить все воркспейсы и восстановить предустановленные шаблоны (Wiki, Commerce, NewsBlog).",
            "en" to "You can reset all cached workspaces and restore pre-configured samples (Wiki, Commerce, NewsBlog).",
            "uk" to "Ви можете скинути всі воркспейси та відновити попередньо встановлені шаблони (Wiki, Commerce, NewsBlog)."
        ),
        "reset_db_btn" to mapOf(
            "ru" to "Очистить кэш базы данных MetaCMS",
            "en" to "Clear MetaCMS database cache",
            "uk" to "Очистити кеш бази даних MetaCMS"
        ),
        "toast_db_cleared" to mapOf(
            "ru" to "База данных сброшена! Перезапустите приложение для инициализации демо-проектов.",
            "en" to "Database reset! Reload app to initialize pre-seed showcases.",
            "uk" to "Базу даних скинуто! Перезапустіть додаток для ініціалізації демо-проєктів."
        ),
        "lang_option_system" to mapOf(
            "ru" to "Как в системе",
            "en" to "System adaptive",
            "uk" to "Як у системі"
        ),
        "theme_option_system" to mapOf(
            "ru" to "Адаптивный (Система)",
            "en" to "System adaptive",
            "uk" to "Адаптивний (Система)"
        ),
        "theme_option_day" to mapOf(
            "ru" to "День (Светлая)",
            "en" to "Day (Light theme)",
            "uk" to "День (Світла)"
        ),
        "theme_option_night" to mapOf(
            "ru" to "Ночь (Темная)",
            "en" to "Night (Dark theme)",
            "uk" to "Ніч (Темна)"
        ),
        "theme_option_scheduled" to mapOf(
            "ru" to "По расписанию (6:00 - 20:00)",
            "en" to "Scheduled (6:00 - 20:00)",
            "uk" to "За розкладом (6:00 - 20:00)"
        ),
        "theme_setting_label" to mapOf(
            "ru" to "Интерфейс: Световой режим",
            "en" to "Interface: Light Mode Theme",
            "uk" to "Інтерфейс: Світловий режим"
        ),
        "lang_setting_label" to mapOf(
            "ru" to "Выберите язык приложения",
            "en" to "Select Application Language",
            "uk" to "Оберіть мову додатка"
        ),

        // Projects screen
        "projects_header" to mapOf(
            "ru" to "Управление Проектами",
            "en" to "Project Workspaces",
            "uk" to "Управління Проєктами"
        ),
        "projects_desc" to mapOf(
            "ru" to "Создавайте и переключайтесь между изолированными CMS окружениями и репозиториями разработчика",
            "en" to "Create and switch between isolated CMS workspaces and developer repositories",
            "uk" to "Створюйте та перемикайтеся між ізольованими CMS оточеннями та репозиторіями розробника"
        ),
        "create_new_project" to mapOf(
            "ru" to "Создать Новый Проект",
            "en" to "Create New Workspace",
            "uk" to "Створити Новий Проєкт"
        ),
        "project_name_label" to mapOf(
            "ru" to "Название проекта",
            "en" to "Project Name",
            "uk" to "Назва проєкту"
        ),
        "project_desc_label" to mapOf(
            "ru" to "Описание предназначения проекта",
            "en" to "Project description & goal",
            "uk" to "Опис призначення проєкту"
        ),
        "tech_stack_label" to mapOf(
            "ru" to "Технологический стек (например, Flutter Web PWA)",
            "en" to "Tech Stack (e.g. Flutter Web PWA)",
            "uk" to "Технологічний стек (наприклад, Flutter Web PWA)"
        ),
        "github_repo_label" to mapOf(
            "ru" to "GitHub репозиторий (owner/repo)",
            "en" to "GitHub Repository (owner/repo)",
            "uk" to "GitHub репозиторій (owner/repo)"
        ),
        "github_branch_label" to mapOf(
            "ru" to "Целевая ветка (например, main)",
            "en" to "Target Branch (e.g. main)",
            "uk" to "Цільова гілка (наприклад, main)"
        ),
        "btn_add_workspace" to mapOf(
            "ru" to "Зарегистрировать Воркспейс CMS",
            "en" to "Register CMS Workspace",
            "uk" to "Зареєструвати Воркспейс CMS"
        ),
        "btn_delete" to mapOf(
            "ru" to "Удалить",
            "en" to "Delete",
            "uk" to "Видалити"
        ),
        "card_repo" to mapOf(
            "ru" to "Репозиторий",
            "en" to "Repo",
            "uk" to "Репозиторій"
        ),
        "card_branch" to mapOf(
            "ru" to "Ветка",
            "en" to "Branch",
            "uk" to "Гілка"
        ),
        "toast_fill_fields" to mapOf(
            "ru" to "Пожалуйста, заполните Имя проекта и Репозиторий!",
            "en" to "Please fill in Project Name and Repository!",
            "uk" to "Будь ласка, заповніть Ім'я проєкту та Репозиторій!"
        ),

        // Schema & modules screen
        "modules_header" to mapOf(
            "ru" to "Конфигуратор Архитектуры СУБД",
            "en" to "DB Architecture Configurator",
            "uk" to "Конфігуратор Архітектури СУБД"
        ),
        "modules_desc" to mapOf(
            "ru" to "Активируйте встроенные функциональные модули CMS и проектируйте структуру базы данных",
            "en" to "Toggle pre-built CMS modules and orchestrate custom table database schemas",
            "uk" to "Активуйте вбудовані функціональні модулі CMS та проєктуйте структуру бази даних"
        ),
        "active_modules_card_title" to mapOf(
            "ru" to "Активированные Модули Системы",
            "en" to "Activated CMS Modules",
            "uk" to "Активовані Модулі Системи"
        ),
        "active_modules_card_desc" to mapOf(
            "ru" to "Нажмите на модуль, чтобы включить/отключить его в итоговой Flutter-сборке.",
            "en" to "Click a module card to toggle its presence in the compiled Flutter workspace.",
            "uk" to "Натисніть на модуль, щоб увімкнути/вимкнути його у підсумковій Flutter-збірці."
        ),
        "schema_editor_title" to mapOf(
            "ru" to "Спецификация Схемы БД проекта",
            "en" to "Project DB Schema Blueprint",
            "uk" to "Специфікація Схеми БД проєкту"
        ),
        "schema_editor_desc" to mapOf(
            "ru" to "Ниже представлены динамические колонки таблиц, используемые для автогенерации SQL/Room и Dart-моделей.",
            "en" to "Custom table attributes used to dynamically compile source SQL structures and Dart models.",
            "uk" to "Нижче представлені динамічні колонки таблиць, що використовуються для автогенерації SQL/Room та Dart-моделей."
        ),
        "add_field_btn" to mapOf(
            "ru" to "Добавить Колонку",
            "en" to "Add Schema Column",
            "uk" to "Додати Колонку"
        ),
        "no_schema_fields" to mapOf(
            "ru" to "Колонки схемы не заданы. Вы можете добавить их вручную или задействовать нашего ИИ Архитектора.",
            "en" to "No columns defined yet. Add fields manually or prompt our AI Architect to generate them.",
            "uk" to "Колонки схеми не задані. Ви можете додати їх вручну або задіяти нашого ШІ Архітектора."
        ),
        "sandbox_title" to mapOf(
            "ru" to "Симулятор Живой Базы",
            "en" to "Live Database Sandbox",
            "uk" to "Симулятор Живої Бази"
        ),
        "sandbox_desc" to mapOf(
            "ru" to "Интерактивная песочница для тестирования схемы",
            "en" to "Interactive playground for evaluating your current schema configurations",
            "uk" to "Інтерактивна пісочниця для тестування схеми"
        ),
        "sandbox_module_disabled" to mapOf(
            "ru" to "Модуль отключен. Активируйте его выше в конфигураторе.",
            "en" to "Selected module is inactive. Enable it above in the configurator.",
            "uk" to "Модуль відключено. Активуйте його вище в конфігураторі."
        ),
        "sandbox_no_fields" to mapOf(
            "ru" to "Колонки не заданы. Добавьте поля в 'Спецификации Схемы БД' выше или используйте ИИ-архитектора.",
            "en" to "No system attributes. Add fields in 'Schema Blueprint' or consult the Gemini AI Architect.",
            "uk" to "Колонки не задані. Додайте поля в 'Специфікації Схеми БД' вище або скористайтеся ШІ-архітектором."
        ),
        "sandbox_add_record_title" to mapOf(
            "ru" to "Добавить запись в таблицу",
            "en" to "Append record to table",
            "uk" to "Додати запис до таблиці"
        ),
        "sandbox_btn" to mapOf(
            "ru" to "Создать Тестовую Запись",
            "en" to "Insert Test Record",
            "uk" to "Створити Тестовий Запис"
        ),
        "sandbox_db_table" to mapOf(
            "ru" to "Виртуальная таблица",
            "en" to "Virtual table DB",
            "uk" to "Віртуальна таблиця"
        ),
        "sandbox_db_empty" to mapOf(
            "ru" to "Таблица пуста. Заполните поля выше, чтобы сгенерировать запись.",
            "en" to "Db table is empty. Input values in fields above to insert simulated rows.",
            "uk" to "Таблиця порожня. Заповніть поля вище, щоб згенерувати запис."
        ),
        "dialog_add_field_h" to mapOf(
            "ru" to "Добавить Поле в Таблицу",
            "en" to "Add Field attribute",
            "uk" to "Додати Поле у Таблицю"
        ),
        "field_name_l" to mapOf(
            "ru" to "Название поля (snake_case)",
            "en" to "Field name (snake_case)",
            "uk" to "Назва поля (snake_case)"
        ),
        "field_type_l" to mapOf(
            "ru" to "Тип данных",
            "en" to "Data Type",
            "uk" to "Тип даних"
        ),
        "field_desc_l" to mapOf(
            "ru" to "Документирующее описание",
            "en" to "Doc description for comments",
            "uk" to "Документуючий опис"
        ),
        "btn_cancel" to mapOf(
            "ru" to "Отмена",
            "en" to "Cancel",
            "uk" to "Скасувати"
        ),
        "btn_add" to mapOf(
            "ru" to "Добавить",
            "en" to "Add",
            "uk" to "Додати"
        ),
        "toast_field_name_req" to mapOf(
            "ru" to "Название колонки не может быть пустым!",
            "en" to "Column name cannot be blank!",
            "uk" to "Назва колонки не може бути порожньою!"
        ),

        // AI Assistant Screen
        "ai_header" to mapOf(
            "ru" to "MetaCMS AI Проектировщик",
            "en" to "MetaCMS AI Architect",
            "uk" to "MetaCMS AI Проєктувальник"
        ),
        "ai_desc" to mapOf(
            "ru" to "Спросите искусственный интеллект Gemini составить архитектуру любой CRM системы, магазина или блога. Генератор автоматически применит SQL структуру и код.",
            "en" to "Prompt Google Gemini to model state rules, CRM pipelines, or shops. The AI will write custom code and inject columns automatically.",
            "uk" to "Запитайте штучний інтелект Gemini скласти архітектуру будь-якої CRM системи, магазину чи блогу. Генератор автоматично застосує SQL структуру та код."
        ),
        "ai_input_placeholder" to mapOf(
            "ru" to "Опишите вашу идею (например: 'База данных для автосалона по продаже электромобилей с маржой')...",
            "en" to "Describe your module or CMS goal (e.g., 'EV vehicle warehouse tracker with retail profit margin field')...",
            "uk" to "Опишіть вашу ідею (наприклад: 'База даних для автосалону з продажу електромобілів з маржою')..."
        ),
        "ai_generate_btn" to mapOf(
            "ru" to "Спроектировать с ИИ разработчиком",
            "en" to "Architect with Gemini AI Developer",
            "uk" to "Спроектувати з ШІ розробником"
        ),
        "ai_recommendations" to mapOf(
            "ru" to "Рекомендованные шаблоны запросов:",
            "en" to "Recommended prompt blueprints:",
            "uk" to "Рекомендовані шаблони запитів:"
        ),
        "ai_resp_title" to mapOf(
            "ru" to "Сводки и исходники от Gemini Architect:",
            "en" to "Gemini Architect Specs & Output:",
            "uk" to "Зведення та вихідні коди від Gemini Architect:"
        ),

        // Github Screen
        "git_header" to mapOf(
            "ru" to "Simulated Git & Actions CI/CD",
            "en" to "Simulated Git & Actions CI/CD",
            "uk" to "Simulated Git & Actions CI/CD"
        ),
        "git_desc" to mapOf(
            "ru" to "Управляйте коммитами, ветками и просматривайте журналы сборщика деплоя GitHub Pages в реальном времени",
            "en" to "Manage commits, local branches and stream live deployment logs for integrated Flutter GitHub Actions workflows",
            "uk" to "Керуйте комітами, гілками та переглядайте журнали збирача деплою GitHub Pages у реальному часі"
        ),
        "git_ops_card" to mapOf(
            "ru" to "Доступные Git Операции",
            "en" to "Available Git Operations",
            "uk" to "Доступні Git Операції"
        ),
        "git_commit_btn" to mapOf(
            "ru" to "Создать Локальный Коммит",
            "en" to "Create Local Git Commit",
            "uk" to "Створити Локальний Коміт"
        ),
        "git_push_btn" to mapOf(
            "ru" to "Push в Удаленный Репозиторий",
            "en" to "Push to Cloud Origin",
            "uk" to "Push у Віддалений Репозиторій"
        ),
        "git_pull_btn" to mapOf(
            "ru" to "Pull Последние Патчи",
            "en" to "Pull Latest Increments",
            "uk" to "Pull Остання Патчі"
        ),
        "git_release_title" to mapOf(
            "ru" to "Генератор Версионных Релизов",
            "en" to "Production Tag/Release Generator",
            "uk" to "Генератор Версійних Релізів"
        ),
        "git_release_tag_l" to mapOf(
            "ru" to "Тег версии (e.g., v1.4.0)",
            "en" to "Version tag (e.g., v1.4.0)",
            "uk" to "Тег версії (e.g., v1.4.0)"
        ),
        "git_release_notes_l" to mapOf(
            "ru" to "Описание изменений (Changelog notes)",
            "en" to "Changelog release notes description",
            "uk" to "Опис змін (Changelog notes)"
        ),
        "git_release_btn" to mapOf(
            "ru" to "Собрать и опубликовать релиз в GitHub",
            "en" to "Compile & publish official release to GitHub",
            "uk" to "Зібрати та опублікувати реліз у GitHub"
        ),
        "git_terminal" to mapOf(
            "ru" to "Журнал Терминала (.git Console Log)",
            "en" to "Terminal output trace (.git Console Log)",
            "uk" to "Журнал Терміналу (.git Console Log)"
        ),
        "git_build_log" to mapOf(
            "ru" to "Лог сборки и компиляции GitHub Workflow",
            "en" to "GitHub Workflow live builder output trace",
            "uk" to "Лог збірки та компіляції GitHub Workflow"
        ),
        "toast_release_created" to mapOf(
            "ru" to "Успешно верифицировано! Тэг релиза отправлен в GitHub.",
            "en" to "Successfully verified! Version tag pushed to origin cloud.",
            "uk" to "Успішно верифіковано! Тег релізу відправлено в GitHub."
        )
    )

    fun translate(key: String, languageCode: String): String {
        val entry = translations[key] ?: return key
        return entry[languageCode] ?: entry["ru"] ?: key
    }

    fun mapSystemLanguage(locale: Locale): String {
        val sysLang = locale.language.lowercase()
        return if (sysLang == "ru" || sysLang == "uk" || sysLang == "en") sysLang else "ru"
    }
}
