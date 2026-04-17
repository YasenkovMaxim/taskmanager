REST API для управления пользователями, проектами и задачами.

---

## 📋 Оглавление

- [О проекте](#о-проекте)
- [Технологии](#технологии)
- [Функциональность](#функциональность)
- [Структура проекта](#структура-проекта)
- [Схема базы данных](#схема-базы-данных)
- [API эндпоинты](#api-эндпоинты)
- [Аутентификация и авторизация](#аутентификация-и-авторизация)
- [Права доступа](#права-доступа)
- [Установка и запуск](#установка-и-запуск)
- [Тестирование](#тестирование)
- [Документация](#документация)
- [Мониторинг](#мониторинг)
- [Планы по улучшению](#планы-по-улучшению)
- [Автор](#автор)

---

## 📝 О проекте

**Task Manager API** — это полнофункциональное бэкенд-приложение для управления задачами. Позволяет создавать проекты, ставить задачи, назначать исполнителей и отслеживать статус выполнения.

Проект разработан в рамках дипломной работы. Основная цель — создание надёжного, масштабируемого и безопасного REST API с использованием современных технологий Java.

---

## 🛠 Технологии

| Категория | Технологии |
|-----------|------------|
| **Язык** | Java 21 |
| **Фреймворк** | Spring Boot 4.0.3 |
| **Безопасность** | Spring Security, JWT, BCrypt |
| **База данных** | PostgreSQL, H2 (тесты) |
| **ORM** | Spring Data JPA, Hibernate |
| **Документация** | Swagger / OpenAPI 3.0 |
| **Мониторинг** | Spring Boot Actuator |
| **Логирование** | SLF4J + Lombok |
| **Сборка** | Maven |
| **Тестирование** | JUnit 5, Mockito |

---

## ⚙ Функциональность

### Пользователи
- Регистрация и аутентификация (JWT)
- Просмотр профиля (свой/чужой — с разными правами)
- Обновление и удаление профиля
- Шифрование паролей (BCrypt)

### Проекты
- Создание, просмотр, обновление, удаление
- Привязка к владельцу (User)
- Защита от несанкционированного доступа

### Задачи
- Создание, просмотр, обновление, удаление
- Статусы: `TODO`, `IN_PROGRESS`, `DONE`
- Приоритеты: `LOW`, `MEDIUM`, `HIGH`
- Дедлайны
- Привязка к проекту и исполнителю

### Безопасность
- JWT токены (время жизни 24 часа)
- Роли: `ADMIN` и `USER`
- Проверка прав доступа на уровне сервисов
- Пароли хранятся в зашифрованном виде (BCrypt)

---

## 🗄 Схема базы данных

```sql
-- Таблица пользователей
users (
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    age        INTEGER,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(50)  NOT NULL,
    created    TIMESTAMP,
    updated    TIMESTAMP
)

-- Таблица проектов
projects (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    owner_id    INTEGER NOT NULL REFERENCES users(id),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
)

-- Таблица задач
tasks (
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(50)  NOT NULL,
    priority    VARCHAR(50)  NOT NULL,
    due_date    TIMESTAMP,
    project_id  INTEGER NOT NULL REFERENCES projects(id),
    assignee_id INTEGER REFERENCES users(id),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
)
Связи между таблицами
users (1) → projects (M) — один пользователь создаёт много проектов

projects (1) → tasks (M) — один проект содержит много задач

users (1) → tasks (M) — один пользователь назначен на много задач

🌐 API эндпоинты
Аутентификация
Метод	URL    Описание
POST	/auth/register	Регистрация пользователя
POST	/auth/login	Вход (возвращает JWT токен)
Пользователи
Метод	URL	Описание	Доступ
GET	/users	Все пользователи (пагинация)	ADMIN
GET	/users/{id}	Пользователь по ID	ADMIN / владелец
GET	/users/email/{email}	Пользователь по email	ADMIN / владелец
POST	/users	Создание пользователя	ADMIN
PUT	/users/{id}	Обновление пользователя	ADMIN / владелец
DELETE	/users/{id}	Удаление пользователя	ADMIN / владелец
Проекты
Метод	URL	Описание	Доступ
GET	/projects	Все проекты (пагинация)	ADMIN
GET	/projects/{id}	Проект по ID	ADMIN / владелец
POST	/projects	Создание проекта	любой
PUT	/projects/{id}	Обновление проекта	ADMIN / владелец
DELETE	/projects/{id}	Удаление проекта	ADMIN / владелец
Задачи
Метод	URL	Описание	Доступ
GET	/tasks	Все задачи (пагинация)	ADMIN
GET	/tasks/{id}	Задача по ID	ADMIN / исполнитель
GET	/tasks/project/{projectId}	Задачи по проекту	ADMIN / владелец проекта
GET	/tasks/assignee/{assigneeId}	Задачи по исполнителю	ADMIN / сам пользователь
POST	/tasks	Создание задачи	любой
PUT	/tasks/{id}	Обновление задачи	ADMIN / исполнитель
DELETE	/tasks/{id}	Удаление задачи	ADMIN / исполнитель
🔐 Аутентификация и авторизация
JWT токен
При успешном логине сервер возвращает JWT токен:

json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresAt": 1776181198576
}
Использование токена
Добавьте токен в заголовок каждого защищённого запроса:

text
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Время жизни токена
Токен действителен 24 часа. После истечения необходимо выполнить логин заново.

👥 Права доступа
Действие	               USER	ADMIN
Просмотр всех пользователей	❌	✅
Просмотр своего профиля	        ✅	✅
Просмотр чужого профиля	        ❌	✅
Обновление себя	                ✅	✅
Обновление другого	        ❌	✅
Удаление себя	                ✅	✅
Удаление другого	        ❌	✅
Просмотр всех проектов	        ❌	✅
Просмотр своих проектов	        ✅	✅
Просмотр чужих проектов	        ❌	✅
Создание проекта	        ✅	✅
Обновление своего проекта	✅	✅
Обновление чужого проекта	❌	✅
Удаление своего проекта   	✅	✅
Удаление чужого проекта	        ❌	✅
Просмотр всех задач      	❌	✅
Просмотр своих задач	        ✅	✅
Просмотр чужих задач	        ❌	✅
Создание задачи              	✅	✅
Обновление своей задачи	        ✅	✅
Обновление чужой задачи   	❌	✅
Удаление своей задачи	        ✅	✅
Удаление чужой задачи     	❌	✅
🚀 Установка и запуск
Требования
JDK 21

PostgreSQL 14+

Maven 3.8+

Шаг 1: Клонировать репозиторий
bash
git clone https://github.com/yourusername/taskmanager.git
cd taskmanager
Шаг 2: Создать базу данных
sql
CREATE DATABASE taskmanager;
Шаг 3: Настроить переменные окружения
properties
db_url=jdbc:postgresql://localhost:5432/taskmanager
db_username=postgres
db_password=root
Шаг 4: Собрать проект
bash
mvn clean package
Шаг 5: Запустить приложение
bash
mvn spring-boot:run
Приложение запустится на http://localhost:8080

🧪 Тестирование
Запуск тестов
bash
mvn test
Покрытие тестами
UserServiceTest — 7 тестов

ProjectServiceTest — 2 теста

TaskServiceTest — 3 теста

UserControllerTest — 2 теста

Примеры запросов
Регистрация
bash
POST /auth/register
{
    "firstName": "Иван",
    "lastName": "Петров",
    "age": 25,
    "email": "ivan@mail.com",
    "password": "12345678"
}
Логин
bash
POST /auth/login
{
    "email": "ivan@mail.com",
    "password": "12345678"
}
Создание проекта (с токеном)
bash
POST /projects
Authorization: Bearer <token>
{
    "name": "Мой первый проект",
    "description": "Описание проекта",
    "ownerId": 1
}
Создание задачи
bash
POST /tasks
{
    "title": "Сделать домашнее задание",
    "description": "Описание",
    "status": "TODO",
    "priority": "HIGH",
    "projectId": 1,
    "assigneeId": 1
}
📖 Документация
Swagger UI
После запуска приложения откройте в браузере:

text
http://localhost:8080/swagger-ui/index.html
Swagger позволяет:

Просматривать все эндпоинты

Тестировать API прямо из браузера

Авторизоваться с помощью JWT токена (кнопка Authorize)

OpenAPI JSON
text
http://localhost:8080/v3/api-docs

👤 Автор
Максим Ясенков
Java Developer

📄 Лицензия
Проект создан в учебных целях.