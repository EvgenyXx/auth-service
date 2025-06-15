# Используем базовый образ с Java 17
FROM eclipse-temurin:17-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл (предварительно собранный локально)
COPY target/auth-service-*.jar app.jar

# Копируем ресурсы (если нужны внешние конфиги/ключи)
COPY src/main/resources/ /app/resources/

# Точка входа (можно добавить параметры JVM при необходимости)
ENTRYPOINT ["java", "-jar", "app.jar"]