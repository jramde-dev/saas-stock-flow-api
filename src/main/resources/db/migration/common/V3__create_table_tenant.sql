CREATE TABLE jr_tenants
(
    id              VARCHAR(255)                NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    created_by      VARCHAR(255)                NOT NULL,
    updated_by      VARCHAR(255),
    deleted         BOOLEAN                     NOT NULL,
    company_name    VARCHAR(255),
    company_code    VARCHAR(255),
    email           VARCHAR(255),
    status          VARCHAR(255),
    admin_full_name VARCHAR(255),
    admin_email     VARCHAR(255),
    admin_username  VARCHAR(255),
    admin_password  VARCHAR(255),
    CONSTRAINT pk_jr_tenants PRIMARY KEY (id)
);

ALTER TABLE jr_users
    ADD tenant_id varchar(255);