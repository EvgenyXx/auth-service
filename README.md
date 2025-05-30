# Auth Service

**Auth Service** — это микросервис, реализующий функциональность регистрации и авторизации пользователей.

## 🔧 Технологии

- Java 17
- Spring Boot 3.5.0
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- MapStruct
- Kafka + Schema Registry (Avro)
- JWT
- Lombok

## 📌 Возможности

- Регистрация пользователей с валидацией e-mail и номера телефона
- Шифрование пароля с использованием `PasswordEncoder`
- Генерация JWT-токенов
- Отправка событий в Kafka (например, `user-created`)
- Миграции БД через Flyway

## 🛠 Запуск

1. Убедитесь, что PostgreSQL и Kafka с Schema Registry запущены.
2. Установите переменные окружения (например, через `.env`).
3. Соберите и запустите проект:

```bash
./mvnw spring-boot:run
