# 💬 Online Chat Project

Многопоточный консольный чат на сокетах с логированием и тестами.

> **Проект выполнен в рамках курса «Профессия Java-разработчик» (Netology)**

---

## 📋 О проекте

Простой многопоточный чат: клиенты подключаются к серверу, обмениваются сообщениями в реальном времени, видят уведомления о подключениях/отключениях.

### ✨ Возможности
- 🔗 Подключение по TCP-сокетам
- 👥 Многопользовательский режим (broadcast)
- 📝 Логирование в файл `file.log`
- ⚙️ Порт сервера из `settings.txt`
- 🧪 Юнит-тесты (JUnit 5)
- 🔤 UTF-8 (кириллица)
- 🚪 Команда `/exit`

---

## 🛠️ Технологии

- Java 21
- Maven
- java.net.Socket / ServerSocket
- java.util.logging
- UTF-8 кодировка (поддержка кириллицы)
- JUnit 5
- Многопоточность (Thread, synchronized)

## 🚀 Запуск

```bash
# Сборка
mvn clean compile

# Тесты
mvn test

# Запуск сервера
mvn exec:java
# или
java -cp target/classes org.example.Server

# Запуск клиента (в новом терминале)
java -cp target/classes org.example.ChatClient
