CREATE DATABASE IF NOT EXISTS e_commerce;

CREATE TABLE IF NOT EXISTS `category`
(
    category_id   INT                 NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(255) UNIQUE NOT NULL,
    description   VARCHAR(255),
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (category_id)
);

CREATE TABLE IF NOT EXISTS `customer`
(
    customer_id INT                 NOT NULL AUTO_INCREMENT,
    first_name  VARCHAR(255)        NOT NULL,
    last_name   VARCHAR(255)        NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL,
    username    VARCHAR(255),
    password    VARCHAR(255)        NOT NULL,
    phone       VARCHAR(15) UNIQUE,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_id)
);


CREATE TABLE IF NOT EXISTS `order`
(
    order_id    INT            NOT NULL AUTO_INCREMENT,
    customer_id INT            NOT NULL,
    order_date  DATETIME DEFAULT CURRENT_TIMESTAMP,
    status      ENUM ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED'),
    total       decimal(10, 2) NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (order_id),
    FOREIGN KEY (customer_id) REFERENCES `customer` (customer_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS `product`
(
    product_id          INT            NOT NULL AUTO_INCREMENT,
    category_id         INT            NOT NULL,
    product_name        VARCHAR(255)   NOT NULL,
    product_description VARCHAR(255)   NOT NULL,
    price               decimal(10, 2) NOT NULL,
    stock               INT      DEFAULT 0,
    created_at          DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES `category` (category_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `order_product`
(
    order_product_id INT NOT NULL AUTO_INCREMENT,
    order_id         INT NOT NULL,
    product_id       INT NOT NULL,
    assigned_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (order_product_id),
    FOREIGN KEY (order_id) REFERENCES `order` (order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES `product` (product_id) ON DELETE CASCADE
);


ALTER TABLE `customer`
    ADD INDEX `username` (`username`);

ALTER TABLE `product`
    ADD INDEX `name` (`product_name`),
    ADD INDEX `price` (`price`),
    ADD INDEX `stock` (`stock`);


SHOW INDEXES FROM product