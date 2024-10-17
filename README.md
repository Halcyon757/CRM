# CRM - система
## Описание проекта
Простая CRM-система на Java с использованием Spring Boot и PostgreSQL, позволяет управлять продавцами и их транзакциями, предоставляет методы для аналитики. Стек: Java, Spring Boot, Spring Data JPA, PostgreSQL, Gradle, JUnit, Mockito, Docker.
## Основные классы
| Наименование класса/интерфейса          | Описание                                                                                                                                        |
|-----------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| Seller, Transaction                     | Сущности представляющие продавца и транзакцию.                                                                                                  |
| SellerController, TransactionController | REST-контроллеры для работы с сущностями продавца и транзакции.                                                                                 |
| SellerService, TransactionService       | Сервисы для работы с бизнес-логикой. Включают методы в соответствии с заданием.                                                                 |
| SellerRepository, TransactionRepository                      | Репозитории для доступа к данным. Наследуются от Spring Data JPA и предоставляют методы для работы с базой данных.                              |
| GlobalExceptionHandler                        | Класс для централизованной обработки ошибок в приложении. Обрабатывает исключения и возвращает осмысленные сообщения об ошибках в формате JSON. |
### Необходимое программное обеспечение

Перед запуском проекта убедитесь, что на вашем компьютере установлены:

- [Docker](https://www.docker.com/products/docker-desktop)
- [Postman](https://www.postman.com/downloads/)
## Запуск проекта
Выполните клонирование проекта
```bash
git clone https://github.com/username/repository-name.git
```
Перейдите в директорию проекта
```bash
cd repository-name
```
Запустите проект с помощью docker compose
```bash
docker-compose up --build
```

## Доступ к pgAdmin

Вы можете использовать **pgAdmin** для работы с базой данных PostgreSQL. Чтобы войти в pgAdmin, используйте следующие данные:

- **URL для pgAdmin**: [http://localhost:5050](http://localhost:5050)
- **Email для входа**: `admin@example.com`
- **Пароль для входа**: `admin`

### Шаги для подключения в pgAdmin:

1. После входа в pgAdmin, нажмите правой кнопкой мыши на **Servers** -> **Register** -> **Server**.
2. Введите имя сервера, например: `CRM`.
3. Во вкладке **Connection** введите следующие данные:
    - **Host name/address**: `db`
    - **Port**: `5432`\
    - **Maintenance database**: `crm_db`
    - **Username**: `postgres`
    - **Password**: `postgres`
4. Нажмите **Save**.
## Примеры использования API
Система не имеет UI, используйте Postman для отправки запросов
### Продавцы (Sellers)

### 1. Получить список всех продавцов
- **URL**: `/api/sellers`
- **Метод**: `GET`
- **Описание**: Возвращает список всех продавцов.
#### Пример запроса
```plaintext
http://localhost:8080/api/sellers
```
### 2. Получить информацию о продавце по ID
- **URL**: `/api/sellers/{id}`
- **Метод**: `GET`
- **Описание**: Возвращает информацию о продавце по его ID.
- **Параметры**:
    - `id` (Long) – ID продавца.
#### Пример запроса
```plaintext
http://localhost:8080/api/sellers/2
```
### 3. Создать нового продавца
- **URL**: `/api/sellers`
- **Метод**: `POST`
- **Описание**: Создает нового продавца.
- **Тело запроса** (JSON):
  ```json
  {
    "name": "Обновленное Имя",
    "contactInfo": "Обновленная Контактная информация",
    "registrationDate": "2024-01-01T12:00:00"
  }
#### Пример запроса
```plaintext
http://localhost:8080/api/sellers
```
### 4. Обновить информацию о продавце
- **URL**: `/api/sellers/{id}`
- **Метод**: `PUT`
- **Описание**: Обновляет информацию о продавце.
- **Тело запроса** (JSON):
  ```json
  {
  "name": "Обновленное Имя",
  "contactInfo": "Обновленная Контактная информация",
  "registrationDate": "2024-01-01T12:00:00"
  }
#### Пример запроса
```plaintext
http://localhost:8080/api/sellers/3
```
### 5. Удалить продавца
- **URL**: `/api/sellers/{id}`
- **Метод**: `DELETE`
- **Описание**: Удаляет продавца по ID.
- **Параметры**:
    - `id` (Long) – ID продавца.
#### Пример запроса
```plaintext
http://localhost:8080/api/sellers/3
```
### 6. Получить самого продуктивного продавца за указанный период
- **URL**: `/api/sellers/top-seller`
- **Метод**: `GET`
- **Описание**: Возвращает самого продуктивного продавца за указанный период.
- **Параметры**:
    - `startDate` (LocalDateTime) – Начало периода.
    - `endDate` (LocalDateTime) – Конец периода.
#### Пример запроса
```plaintext
http://localhost:8080/api/sellers/top-seller?startDate=2023-01-01T00:00:00&endDate=2024-01-01T23:59:59
```
### 7. Получить продавцов с суммой транзакций меньше указанного значения
- **URL**: `/api/sellers/less-than`
- **Метод**: `GET`
- **Описание**: Возвращает продавцов с суммой транзакций меньше указанного значения за указанный период.
- **Параметры**:
    - `amount` (Double) – Максимальная сумма транзакций.
    - `startDate` (LocalDateTime) – Начало периода.
    - `endDate` (LocalDateTime) – Конец периода.
#### Пример запроса
```plaintext
http://localhost:8080/api/sellers/less-than?amount=4000&startDate=2023-01-01T00:00:00&endDate=2024-01-01T23:59:59
```
### 8. Получить лучший период транзакций для продавца
- **URL**: `/api/sellers/{sellerId}/best-period`
- **Метод**: `GET`
- **Описание**: Возвращает лучший период транзакций для продавца.
- **Параметры**:
    - `sellerId` (Long) – ID продавца.
#### Пример запроса
```plaintext
http://localhost:8080/api/sellers/1/best-period
```
### 9. Получить список всех транзакций
- **URL**: `/api/transactions`
- **Метод**: `GET`
- **Описание**: Возвращает список всех транзакций.
#### Пример запроса
```plaintext
http://localhost:8080/api/transactions
```
### 10. Получить информацию о транзакции по ID
- **URL**: `/api/transactions/{id}`
- **Метод**: `GET`
- **Описание**: Возвращает информацию о транзакции по ее ID.
- **Параметры**:
    - `id` (Long) – ID транзакции.
#### Пример запроса
```plaintext
http://localhost:8080/api/transactions/1
```
### 11. Создать новую транзакцию
- **URL**: `/api/transactions`
- **Метод**: `POST`
- **Описание**: Создает новую транзакцию.
- **Тело запроса** (JSON):
  ```json
  {
    "seller": { "id": 1 },
    "amount": 1000.0,
    "paymentType": "CASH",
    "transactionDate": "2024-01-01T15:00:00"
  }
#### Пример запроса
```plaintext
http://localhost:8080/api/transactions
```
### 12. Получить все транзакции продавца по ID
- **URL**: `/api/transactions/seller/{sellerId}`
- **Метод**: `GET`
- **Описание**: Возвращает список транзакций для указанного продавца.
- **Параметры**:
    - `sellerId` (Long) – ID продавца.
#### Пример запроса
```plaintext
http://localhost:8080/api/transactions/seller/1
```
### 13. Получить транзакции продавца за указанный период
- **URL**: `/api/transactions/seller/{sellerId}/period`
- **Метод**: `GET`
- **Описание**: Возвращает транзакции продавца за указанный период.
- **Параметры**:
    - `sellerId` (Long) – ID продавца.
    - `start` (LocalDateTime) – Начало периода.
    - `end` (LocalDateTime) – Конец периода.
#### Пример запроса
```plaintext
http://localhost:8080/api/transactions/seller/1/period?start=2023-01-01T00:00:00&end=2024-01-01T23:59:59
```
## Обработка ошибок
В проекте реализована централизованная обработка ошибок в классе GlobalExceptionHandler, которая возвращает JSON-ответы с описанием ошибки для некоторых методов, и сообщение об ошибке на сервере для остальных.