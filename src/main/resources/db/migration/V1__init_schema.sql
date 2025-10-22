CREATE TABLE users (
    id          CHAR(36)        NOT NULL    DEFAULT (UUID())            PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL,
    email       VARCHAR(100)    NOT NULL,
    password    VARCHAR(100)    NOT NULL,
    bio         VARCHAR(255)    NULL,
    image_key   VARCHAR(100)    NULL,
    role        VARCHAR(10)     NOT NULL,
    created_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP   ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP       NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
CREATE UNIQUE INDEX unique_active_user_email
ON users((CASE WHEN deleted_at IS NULL THEN email ELSE NULL END));
CREATE UNIQUE INDEX unique_active_user_image
ON users((CASE WHEN deleted_at IS NULL THEN image_key ELSE NULL END));

CREATE TABLE product_categories (
    id          CHAR(36)        NOT NULL    DEFAULT (UUID())            PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL,
    created_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP   ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP       NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE products (
    id          CHAR(36)        NOT NULL    DEFAULT (UUID())            PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL,
    price       DECIMAL(10, 2)  NOT NULL,
    stock       INT             NOT NULL,
    image_key   VARCHAR(100)    NULL,
    category_id CHAR(36)        NOT NULL,
    created_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP   ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP       NULL,

    CONSTRAINT fk_products_product_categories
        FOREIGN KEY (category_id)
        REFERENCES product_categories(id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
CREATE UNIQUE INDEX unique_active_product_image
ON products((CASE WHEN deleted_at IS NULL THEN image_key ELSE NULL END));

CREATE TABLE orders (
    id          CHAR(36)        NOT NULL    DEFAULT (UUID())            PRIMARY KEY,
    total_price DECIMAL(10, 2)  NOT NULL,
    status      VARCHAR(10)     NOT NULL,
    user_id     CHAR(36)        NOT NULL,
    created_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP   ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP       NULL,

    CONSTRAINT fk_orders_users
        FOREIGN KEY (user_id)
        REFERENCES users(id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE order_items (
    id          CHAR(36)        NOT NULL    DEFAULT (UUID())            PRIMARY KEY,
    quantity    INT             NOT NULL,
    order_id    CHAR(36)        NOT NULL,
    product_id  CHAR(36)        NOT NULL,
    created_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP   ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP       NULL,

    CONSTRAINT fk_order_items_orders
        FOREIGN KEY(order_id)
        REFERENCES orders(id),
    CONSTRAINT fk_order_items_products
        FOREIGN KEY(product_id)
        REFERENCES products(id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
CREATE UNIQUE INDEX unique_active_order_item
ON order_items(
    (CASE WHEN deleted_at IS NULL THEN order_id ELSE NULL END),
    (CASE WHEN deleted_at IS NULL THEN product_id ELSE NULL END)
);