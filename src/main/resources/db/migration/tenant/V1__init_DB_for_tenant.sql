CREATE TABLE jr_categories
(
    id          VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  VARCHAR(255) NOT NULL,
    updated_by  VARCHAR(255),
    deleted     BOOLEAN      NOT NULL,
    name        VARCHAR(255),
    description TEXT,
    CONSTRAINT pk_jr_categories PRIMARY KEY (id)
);

CREATE TABLE jr_products
(
    id              VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    created_by      VARCHAR(255) NOT NULL,
    updated_by      VARCHAR(255),
    deleted         BOOLEAN      NOT NULL,
    name            VARCHAR(255),
    reference       VARCHAR(255),
    description     VARCHAR(255),
    alert_threshold INTEGER,
    price           DECIMAL,
    category_id     VARCHAR(255),
    CONSTRAINT pk_jr_products PRIMARY KEY (id)
);

CREATE TABLE jr_stock_mvmts
(
    id         VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255) NOT NULL,
    updated_by VARCHAR(255),
    deleted    BOOLEAN      NOT NULL,
    type_mvmt  VARCHAR(255),
    quantity   INTEGER,
    date_mvmt  TIMESTAMP WITHOUT TIME ZONE,
    comment    VARCHAR(255),
    product_id VARCHAR(255),
    CONSTRAINT pk_jr_stock_mvmts PRIMARY KEY (id)
);

ALTER TABLE jr_products
    ADD CONSTRAINT FK_PRODUCTS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES jr_categories (id);

ALTER TABLE jr_stock_mvmts
    ADD CONSTRAINT FK_MVMTS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES jr_products (id);