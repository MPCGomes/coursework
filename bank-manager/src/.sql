CREATE DATABASE bank;
USE bank;

CREATE TABLE accounts (
    number INT PRIMARY KEY,
    holder VARCHAR(100) NOT NULL,
    balance DECIMAL(10,2) NOT NULL
);

INSERT INTO accounts (number, holder, balance) VALUES
(101, 'Marcos', 10000.00),
(102, 'Gika',   7500.00),
(103, 'Murillo', 5000.00);