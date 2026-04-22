CREATE DATABASE IF NOT EXISTS personal_finance;

USE personal_finance;

CREATE TABLE `transactions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(10,2) NOT NULL,
  `category` varchar(50) NOT NULL,
  `date` date NOT NULL,
  `description` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);
