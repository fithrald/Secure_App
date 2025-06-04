
# 📘 Secure Notes
Посилання на репозиторій з кодом: https://github.com/fithrald/Secure_App

> Ідея проекту «Secure Notes» полягає в створенні веб-додатку для безпечного зберігання особистих нотаток. Головна мета — гарантувати конфіденційність та цілісність даних за допомогою шифрування, двофакторної аутентифікації та захисту від таких загроз, як XSS, CSRF, SQL-ін’єкції та DoS.


---

## 👤 Автор

- **ПІБ**: Борщев Максим
- **Група**: ФЕІ-41
- **Керівник**: асис. Шмигельський Я.А
- **Дата виконання**: 04.05.2025

---

## 📌 Загальна інформація

- **Тип проєкту**: Вебдодаток
- **Мова програмування**: Java, JavaScript, HTML, CSS
- **Фреймворки / Бібліотеки**: Spring, PostgreSQL, Docker, Apache Maven

---

## 🧠 Опис функціоналу

- 🗒️ Створення, редагування, перегляд, видалення нотаток
- 💾 Збереження даних у базу даних PostgreSQL
- 🌐 Spring MVC для взаємодії між frontend та backend
- 🔐 Авторизація та двохфакторна аутентифікація через додаток Google Authenticator
- 🔐 Шифрування користувацьких даних та усієї інформації про нотатки
- 🔐 Захист від таких загроз, як XSS, CSRF, SQL-ін’єкції та DoS
- 📱 Інтерфейс з кнопками для роботи з нотатками

---

## 🧱 Опис основних класів / файлів

| Клас / Файл     | Призначення |
|----------------|-------------|
| `Application.java`                                                 | Точка входу backend 
| `application.properties`                                           | Налаштування додатку 
| `com.example.demo.config.SecurityConfig.java`                      | Конфігурація Spring Security
| `com.example.demo.controllers.AuthController.java`                 | Контролер для входу, реєстрації, обробки 2FA
| `com.example.demo.controllers.PostController.java`                 | Контролер для CRUD‐операцій з нотатками
| `com.example.demo.util.EncryptionConverter.java`                   | Перетворювач для шифрування/дешифрування полів

---

## ▶️ Як запустити проєкт "з нуля"

1. Встановлення інструментів

    -Java 17+ (JDK)
    -Maven 3.6+
    -PostgreSQL (локально або на віддаленому сервері)

2. Клонування репозиторію
    
    git clone https://github.com/fithrald/Secure_App.git
    cd Secure_App


3. Створення бази даних

    Запустіть PostgreSQL (локально або через Docker).
    
    Створіть нову базу, наприклад secure_notes_db, та користувача з усіма правами.
    
    CREATE DATABASE secure_notes_db;
    CREATE USER secure_user WITH ENCRYPTED PASSWORD 'StrongPassword';
    GRANT ALL PRIVILEGES ON DATABASE secure_notes_db TO secure_user;

4. Налаштування application.properties

    Відкрийте файл src/main/resources/application.properties і вкажіть правильні значення підключення до вашої бази:

    # Порт, на якому працює застосунок
    server.port=8080
    
    # Підключення до PostgreSQL
    spring.datasource.url=jdbc:postgresql://localhost:5432/secure_notes_db
    spring.datasource.username=secure_user
    spring.datasource.password=StrongPassword
    
    # JPA / Hibernate
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    
    # Логування
    logging.level.org.springframework=INFO
    logging.level.com.example.demo=DEBUG
    
5. Побудова та запуск

    # Перейдіть у кореневу папку проєкту
    cd Secure_App
    
    # Зібрати артефакти Maven та запустити Spring Boot
    mvn clean package
    mvn spring-boot:run
    
6.Тепер фронтенд буде доступний за адресою:
    
    http://localhost:8080

🔌 API приклади

🔐 Авторизація/ Аутентифікація

    -GET /auth/registration
    Повертає сторінку реєстрації (форма: username, password).
    
    -POST /auth/registration
    Зареєструвати нового користувача.
    
    username=new_user
    password=Pa$$w0rd
    — Після успішної реєстрації відбувається редирект на /auth/login.
    
    -GET /auth/login
    Повертає сторінку входу (форма: username, password).
    
    -POST /process_login
    Перевіряє username та password.
    
    Якщо у користувача увімкнено 2FA → редирект на /2fa
    
    Інакше → редирект на /hello (сторінка “привітання”)
    
    -GET /2fa
    Показує форму для введення 6‐значного TOTP‐коду.
    
    -POST /2fa/verify
    Перевіряє TOTP‐код:
    
    code=834120
    — Якщо вірно: редирект на /hello, інакше повернення з помилкою.
    
    -GET /logout
    Здійснює logout і перекидає на /auth/login.
    GET /post/list
    
📋 CRUD для нотаток
    — Відображає список нотаток (Thymeleaf):     
    <div th:each="post : ${posts}">
        <h3 th:text="${post.title}"></h3>
        <p th:text="${post.text}"></p>
        <!-- Кнопки Edit/Delete поруч -->
    </div>
    Відповідь: HTML‐сторінка templates/post/list.html.
    
    GET /post/new
    — Відображає форму створення нової нотатки (Thymeleaf):
        <form th:action="@{/post}" method="post">
        <input type="text" name="title" placeholder="Заголовок" />
        <textarea name="text" placeholder="Вміст"></textarea>
        <button type="submit">Створити</button>
    </form>
    POST /post
    
    — Обробляє створення нової нотатки.
    Параметри:
    title=Моя перша нотатка
    text=Це тестовий текст
    — Редирект: /post/list.
    
    GET /post/edit/{id}
    — Повертає форму для редагування нотатки з id={id} (Thymeleaf).
    
    POST /post/update/{id}
    — Обробляє оновлення нотатки.
    Параметри:
 
    title=Оновлений заголовок
    text=Оновлений вміст
    DELETE /post/{id}
    — Видаляє нотатку з id={id} (форму видалення можна знаходити як hidden‐form у list.html).

## 🖱️ Інструкція для користувача

    Головна сторінка (GET /)
    — Якщо ви неавторизовані, автоматично редиректить на /auth/login.
    — Після входу потрапляємо на /hello з привітанням і кнопками:
    
    ➕ Створити нотатку → перехід на /post/new
    
    📋 Переглянути нотатки → перехід на /post/list
    
    🚪 Вийти → вихід із системи
    
    Реєстрація (GET /auth/registration)
    — Вводимо username та password, натискаємо “Register”.
    
    Вхід (GET /auth/login)
    — Вводимо username + password.
    — Якщо двофакторна аутентифікація увімкнена → після успішного входу переходимо на /2fa, де треба ввести 6‐значний TOTP‐код.
    — Якщо 2FA вимкнена → відразу переходимо на /hello.
    
    Налаштування 2FA (GET /2fa/setup)
    — Показуємо QR‐код (TOTP‐секрет) та поле для одноразового коду.
    — Користувач сканує код у Google Authenticator → отримує постійно оновлюваний 6‐значний ключ.
    — Вводить перший згенерований код → підтвердження 2FA.
    
    Перегляд нотаток (GET /post/list)
    — Відображається перелік усіх нотаток поточного користувача.
    — Поруч із кожною нотаткою є кнопки: ✏️ Edit та 🗑️ Delete.
    
    Створення нотатки (GET /post/new + POST /post)
    — Форма: заголовок + текст → “Create”.
    
    Редагування нотатки (GET /post/edit/{id} + POST /post/update/{id})
    — Відкривається попередньо заповнена форма → “Save”.
    
    Видалення нотатки (DELETE /post/{id})
    — Підтвердження видалення → видалено.
    
    Вихід із системи (GET /logout)
    — При натисканні “Logout” завершується сесія і повертає на сторінку входу.



## 🧪 Проблеми і рішення

    500 Internal Server Error під час старту   Перевірити правильність налаштувань spring.datasource.url, username, password у application.properties
    Не вистачає прав на створення таблиць	     Переконатися, що користувач БД має привілеї CREATE/UPDATE/DELETE для secure_notes_db
    Помилка SQL при пошуку (SQL‐ін’єкція)	     Використовувати підготовлені запити (JdbcTemplate із ?‐параметрами) замість конкатенації рядків
    Невірний TOTP‐код у 2FA	                Перевірити системний час на сервері і на мобільному (мають бути синхронізовані)
    429 Too Many Requests (rate‐limit)	     Якщо тестуєте DoS: перевірити ліміти у RateLimitFilter (100 запитів/хв) та IP/User‐based ключ


## 🧾 Використані джерела / література

    Spring Boot – офіційна документація (https://spring.io/projects/spring-boot)
    Spring Security – офіційна документація (https://spring.io/projects/spring-security)
    Thymeleaf – офіційна документація (https://www.thymeleaf.org/)
    Jsoup – офіційнa документація для очищення HTML (https://jsoup.org/)
    Bucket4j – документація для rate‐limiting (https://bucket4j.com/)
    PostgreSQL – офіційна документація (https://www.postgresql.org/)



