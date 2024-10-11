CREATE SCHEMA IF NOT EXISTS TRABEAN;

USE TRABEAN;

CREATE TABLE `users` (
    `user_id` bigint NOT NULL AUTO_INCREMENT,  -- AUTO_INCREMENT 추가
    `user_key` VARCHAR(40) UNIQUE NOT NULL,
    `email` VARCHAR(100) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,  -- 암호는 보통 더 긴 해시값을 저장하므로 길이를 늘림
    `name` VARCHAR(10) NOT NULL,
    `payment_account_id` bigint NULL,
    `main_account_id` bigint UNIQUE NULL,
    PRIMARY KEY (`user_id`)
);
