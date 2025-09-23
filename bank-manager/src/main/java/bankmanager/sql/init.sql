create database bank_manager;

use bank_manager;

CREATE TABLE accounts (
    number INT PRIMARY KEY,
    holder VARCHAR(100) NOT NULL,
    balance DECIMAL(10,2) NOT NULL
);