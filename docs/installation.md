# Руководство по установке и сборке (Installation & Build Guide) — MetaCMS Builder 💾

Этот документ описывает шаги по развертыванию среды разработки, сборке APK-пакетов проекта **MetaCMS Builder** и запуску локальной компиляции.

---

## 📋 Предварительные требования

Перед началом сборки убедитесь, что на вашем компьютере установлены следующие компоненты:
1. **Java Development Kit (JDK 17):** Проект спроектирован под спецификацию Java 17. Мы настоятельно рекомендуем использовать дистрибутив **Zulu OpenJDK 17**.
2. **Android SDK & Gradle:** Android SDK Platform 34 и Gradle версии 8.2+.
3. **Android Studio (версия Hedgehog или новее):** Для комфортного написания кода и вызова Compose Preview.

---

## 🚀 Сборка проекта из терминала

1. **Клонирование репозитория:**
   ```bash
   $ git clone https://github.com/andarer/metacms-builder-platform.git
   $ cd metacms-builder-platform
   ```

2. **Запуск очистки кэша и сборки:**
   Для компиляции отладочного APK пакета вызовите стандартную задачу Gradle:
   ```bash
   $ ./gradlew assembleDebug
   ```

3. **Итоговый файл APK** будет находиться по пути:
   `app/build/outputs/apk/debug/app-debug.apk`

---

## 🔑 Конфигурация API ключей Google Gemini

Проект использует искусственный интеллект Gemini для автоматического написания Dart-схем и баз данных:
- Зарегистрируйте ключ на портале [Google AI Studio](https://aistudio.google.com/).
- Добавьте секретную переменную `GEMINI_API_KEY` в **Secrets Panel** в настройках вашей среды Google AI Studio.
- Приложение автоматически подхватит его через сгенерированный класс `BuildConfig`. При локальном тестировании вы можете добавить ключ в переменную окружения системы:
  ```bash
  $ export GEMINI_API_KEY="ваш_секретный_ключ"
  ```
