# Развертывание Docker & PWA (Docker & PWA Deployment Guide) — MetaCMS Builder 🐳

MetaCMS Builder разработан с упором на мгновенную компиляцию схем в отзывчивое веб-окружение Progress Web App (PWA) и его контейнеризацию.

---

## 🐬 Оффлайн работа веб-сайта (Service Worker & Workbox)

Генерируемый компилятором статический сайт включает в себя Service Worker на базе библиотеки **Workbox** от Google. Это позволяет кэшировать основные ассеты и файлы таблиц динамической CMS, предотвращая сбои при потере интернет-соединения:

```javascript
// Конфигурация кэширования статики и API-запросов (modules/pwa_config.js)
importScripts('https://storage.googleapis.com/workbox-cdn/releases/6.4.1/workbox-sw.js');

if (workbox) {
  console.log('Workbox успешно инициализирован в Service Worker!');
  
  // Кэшируем стили и шрифты Google Fonts
  workbox.routing.registerRoute(
    /^https:\/\/fonts\.(?:googleapis|gstatic)\.com/,
    new workbox.strategies.CacheFirst({
      cacheName: 'google-fonts-cache',
      plugins: [
        new workbox.expiration.ExpirationPlugin({
          maxEntries: 20,
          maxAgeSeconds: 30 * 24 * 60 * 60, // 30 дней
        }),
      ],
    })
  );

  // Сетевая стратегия для симулируемого CMS REST API
  workbox.routing.registerRoute(
    new RegExp('/cms-api/'),
    new workbox.strategies.NetworkFirst({
      cacheName: 'metacms-db-cache-v1',
      plugins: [
        new workbox.expiration.ExpirationPlugin({
          maxEntries: 50,
        }),
      ],
    })
  );
}
```

---

## 🏗️ Контейнеризация приложения через Docker

Для запуска готовой веб-версии в изолированном облаке или локальном Docker-окружении мы используем двухэтапную сборку (multi-stage) и веб-сервер **Nginx-Alpine**:

### 1. Содержимое Dockerfile (`docker/Dockerfile`)
```dockerfile
# Сценарий доставки статических страниц MetaCMS
FROM nginx:1.25.3-alpine

# Копируем сгенерированные файлы статического PWA сайта во фронтенд каталог Nginx
COPY ./build/web /usr/share/nginx/html

# Переносим конфигурационные файлы сервера Nginx
COPY ./docker/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

### 2. Запуск через Docker Compose (`docker-compose.yml`)
Для быстрого поднятия локального контейнера на порту 8080 используйте следующий манифест в корне вашего репозитория:

```yaml
version: '3.8'

services:
  metacms-site:
    image: metacms-site:latest
    build:
      context: .
      dockerfile: docker/Dockerfile
    ports:
      - "8080:80"
    restart: always
```

- Выполните следующую команду для сборки и запуска контейнера в фоновом режиме:
  ```bash
  $ docker compose up -d --build
  ```
- Перейдите по адресу `http://localhost:8080`, чтобы проверить работоспособность вашей сгенерированной веб-панели.
