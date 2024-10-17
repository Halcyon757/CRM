-- Решил скриптом добавить данные, придется создать таблицы не дожидаясь мэппинга
-- т.к скрипт выполняется ранее чем происходит отображение в БД

CREATE TABLE sellers (
                         id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                         name VARCHAR(255) NOT NULL,
                         contact_info VARCHAR(255),
                         registration_date TIMESTAMP NOT NULL
);


CREATE TABLE transactions (
                              id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                              seller_id BIGINT NOT NULL,
                              amount DECIMAL(10, 2) NOT NULL,
                              payment_type VARCHAR(255) NOT NULL,
                              transaction_date TIMESTAMP NOT NULL,
                              FOREIGN KEY (seller_id) REFERENCES sellers(id) ON DELETE CASCADE
);


INSERT INTO sellers (name, contact_info, registration_date) VALUES
                                                                ('Иван Иванов', 'ivan.ivanov@example.com', '2023-01-15 10:00:00'),
                                                                ('Сергей Сергеев', 'sergey.sergeev@example.com', '2023-02-10 12:30:00'),
                                                                ('Алексей Алексеев', 'alexey.alekseev@example.com', '2023-03-20 09:15:00'),
                                                                ('Мария Смирнова', 'maria.smirnova@example.com', '2023-04-05 14:45:00'),
                                                                ('Ольга Кузнецова', 'olga.kuznetsova@example.com', '2023-05-25 11:00:00');


INSERT INTO transactions (seller_id, amount, payment_type, transaction_date) VALUES
                                                                                 (1, 1000.50, 'CASH', '2023-01-20 14:00:00'),
                                                                                 (1, 500.00, 'CARD', '2023-02-10 11:15:00'),
                                                                                 (1, 150.75, 'TRANSFER', '2023-03-05 16:30:00');

INSERT INTO transactions (seller_id, amount, payment_type, transaction_date) VALUES
                                                                                 (2, 1200.00, 'CARD', '2023-02-20 10:00:00'),
                                                                                 (2, 450.25, 'CASH', '2023-03-12 13:45:00'),
                                                                                 (2, 350.00, 'TRANSFER', '2023-04-01 09:20:00'),
                                                                                 (2, 800.60, 'CASH', '2023-05-18 17:10:00');

INSERT INTO transactions (seller_id, amount, payment_type, transaction_date) VALUES
                                                                                 (3, 700.80, 'TRANSFER', '2023-03-25 15:00:00'),
                                                                                 (3, 650.40, 'CARD', '2023-04-14 12:45:00');

INSERT INTO transactions (seller_id, amount, payment_type, transaction_date) VALUES
                                                                                 (4, 250.00, 'CASH', '2023-05-02 10:30:00'),
                                                                                 (4, 1200.50, 'CARD', '2023-06-10 09:00:00'),
                                                                                 (4, 900.00, 'TRANSFER', '2023-07-05 11:15:00');

INSERT INTO transactions (seller_id, amount, payment_type, transaction_date) VALUES
                                                                                 (5, 550.25, 'CASH', '2023-08-20 15:00:00'),
                                                                                 (5, 1350.75, 'TRANSFER', '2023-09-15 14:30:00'),
                                                                                 (5, 475.60, 'CARD', '2023-11-01 16:45:00');
