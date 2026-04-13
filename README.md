Название проекта
Task Manager API

Краткое описание
REST API для управления пользователями, проектами и задачами. Позволяет создавать проекты, ставить задачи, назначать исполнителей и отслеживать статус выполнения.

Статус проекта: готово
Java 21
Spring Boot 4.0.3
Тесты: проходят

Содержание:

Технологии

Начало работы

Тестирование
To do
Команда проекта

Технологии:

Java 21
Spring Boot 4.0.3
Spring Data JPA (Hibernate)
Spring Security + JWT
PostgreSQL
H2 (для тестов)
Swagger / OpenAPI
Lombok
Maven
JUnit 5 + Mockito

Начало работы:

Требования:

JDK 21
PostgreSQL 14+
Maven 3.8+

Установка и запуск:

Клонировать репозиторий
git clone https://github.com/YasenkovMaxim/taskmanager
cd taskmanager

Создать базу данных PostgreSQL
CREATE DATABASE taskmanager;

Настроить переменные окружения
db_url=jdbc:postgresql://localhost:5432/taskmanager
db_username=postgres
db_password=root

Собрать проект: mvn clean package
Запустить приложение: mvn spring-boot:run
Приложение запустится на http://localhost:8080
Документация API (Swagger)
http://localhost:8080/swagger-ui/index.html

Тестирование:

Для тестирования используется JUnit 5 и Mockito. Для тестов используется встроенная H2 база данных.

mvn test

Покрытие тестами:

UserService — 6 тестов
ProjectService — 2 теста
TaskService — 3 теста
UserController — 2 теста


To do:

CRUD для пользователей

CRUD для проектов

CRUD для задач

JWT аутентификация

Swagger документация

Actuator мониторинг

Валидация входных данных

Пагинация и сортировка

Unit тесты

Docker + docker-compose

Email уведомления

Фронтенд на React

Команда проекта

Максим Ясенков — Java Developer
GitHub: https://github.com/yourusername
Email: maksim@example.com