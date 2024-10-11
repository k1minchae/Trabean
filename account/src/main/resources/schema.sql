CREATE SCHEMA IF NOT EXISTS trabean;

USE trabean;

CREATE TABLE `users` (
    `user_id` bigint NOT NULL AUTO_INCREMENT,
    `user_key` VARCHAR(40) UNIQUE NOT NULL,
    `email` VARCHAR(100) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `name` VARCHAR(10) NOT NULL,
    `payment_account_id` bigint NULL,
    `main_account_id` bigint UNIQUE NULL,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE `accounts` (
    `account_id` bigint NOT NULL AUTO_INCREMENT,
    `account_no` VARCHAR(16) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`account_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) -- user_id와 연관된 외래 키
);

CREATE TABLE `user_accounts` (
    `user_account_id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `account_id` bigint NOT NULL,
    `role` enum('ADMIN', 'PAYER', 'NONE_PAYER') NOT NULL,
    PRIMARY KEY (`user_account_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`), -- users와 연관된 외래 키
    FOREIGN KEY (`account_id`) REFERENCES `accounts`(`account_id`) -- accounts와 연관된 외래 키
);

CREATE TABLE `krw_travel_accounts` (
    `account_id` bigint NOT NULL AUTO_INCREMENT,
    `account_name` VARCHAR(20) NOT NULL,
    `target_amount` bigint NOT NULL,
    PRIMARY KEY (`account_id`),
    FOREIGN KEY (`account_id`) REFERENCES `accounts`(`account_id`) -- accounts와 연관된 외래 키
);

CREATE TABLE `foreign_travel_accounts` (
    `account_id` bigint NOT NULL AUTO_INCREMENT,
    `exchange_currency` VARCHAR(5) NOT NULL,
    `parent_account_id` bigint NOT NULL,
    PRIMARY KEY (`account_id`),
    FOREIGN KEY (`account_id`) REFERENCES `accounts`(`account_id`), -- accounts와 연관된 외래 키
    FOREIGN KEY (`parent_account_id`) REFERENCES `krw_travel_accounts`(`account_id`)
);

CREATE TABLE `merchants` (
    `merchant_id` bigint NOT NULL AUTO_INCREMENT,
    `account_no` VARCHAR(16) NOT NULL,
    `name` VARCHAR(20) NOT NULL,
    `category` ENUM('TRANSPORTATION', 'FOOD', 'SHOPPING', 'ACCOMMODATION', 'ACTIVITY', 'OTHER') NOT NULL,
    `exchange_currency` VARCHAR(5) NOT NULL,
    PRIMARY KEY (`merchant_id`)
);

CREATE TABLE `payments` (
    `pay_id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `account_id` bigint NULL,
    `merchant_id` bigint NULL,
    `transaction_id` VARCHAR(100) UNIQUE NOT NULL,
    `payment_date` Timestamp NULL,
    `krw_amount` bigint NULL,
    `foreign_amount` double NULL,
    `payment_status` enum('SUCCESS', 'CANCEL', 'EXPIRED', 'PASSWORD_ERROR', 'BALANCE_ERROR', 'PENDING') NOT NULL DEFAULT 'PENDING',
    `password_error_count` int NOT NULL DEFAULT 0,
    PRIMARY KEY (`pay_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`),
    FOREIGN KEY (`account_id`) REFERENCES `accounts`(`account_id`), -- accounts와 연관된 외래 키
    FOREIGN KEY (`merchant_id`) REFERENCES `merchants`(`merchant_id`) -- merchants와 연관된 외래 키
);

CREATE TABLE `invitations` (
    `invitation_id` bigint NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(100) NOT NULL,
    `is_accepted` boolean NOT NULL DEFAULT false,
    `account_id` bigint NOT NULL,
    `invite_date` Timestamp NOT NULL,
    PRIMARY KEY (`invitation_id`),
    FOREIGN KEY (`account_id`) REFERENCES `krw_travel_accounts`(`account_id`) -- accounts와 연관된 외래 키
);

CREATE TABLE `notifications` (
    `notification_id` bigint NOT NULL AUTO_INCREMENT,
    `sender_id` bigint NOT NULL,
    `receiver_id` bigint NOT NULL,
    `type` enum('INVITE', 'DEPOSIT', 'WITHDRAW', 'PAYMENT') NOT NULL,
    `create_time` timestamp NOT NULL,
    `is_read` boolean NOT NULL DEFAULT false,
    `amount` bigint NULL,
    `account_id` bigint NOT NULL,
    PRIMARY KEY (`notification_id`),
    FOREIGN KEY (`sender_id`) REFERENCES `users`(`user_id`), -- users와 연관된 외래 키
    FOREIGN KEY (`receiver_id`) REFERENCES `users`(`user_id`), -- users와 연관된 외래 키
    FOREIGN KEY (`account_id`) REFERENCES `accounts`(`account_id`) -- accounts와 연관된 외래 키
);

CREATE TABLE `payout` (
    `payout_id` bigint NOT NULL AUTO_INCREMENT,
    `merchant_id` bigint NOT NULL,
    `status` enum('SUCCESS', 'FAILED', 'PENDING') NOT NULL DEFAULT 'PENDING',
    PRIMARY KEY (`payout_id`),
    FOREIGN KEY (`merchant_id`) REFERENCES `merchants`(`merchant_id`) -- merchants와 연관된 외래 키
);

ALTER TABLE users
ADD FOREIGN KEY (`main_account_id`) REFERENCES `accounts`(`account_id`) ;


ALTER TABLE users
ADD FOREIGN KEY (`payment_account_id`) REFERENCES `accounts`(`account_id`) ;
