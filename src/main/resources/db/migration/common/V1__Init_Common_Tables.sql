create table jr_tenants
(
    deleted         boolean      not null,
    created_at      timestamp(6) not null,
    updated_at      timestamp(6),
    admin_email     varchar(255),
    admin_full_name varchar(255),
    admin_password  varchar(255),
    admin_username  varchar(255),
    company_code    varchar(255),
    company_name    varchar(255),
    created_by      varchar(255) not null,
    email           varchar(255),
    id              varchar(255) not null primary key,
    status          varchar(255)
        constraint jr_tenants_status_check check ((status)::text = ANY
        ((ARRAY [
        'PENDING':: character varying,
        'ACTIVE':: character varying,
        'SUSPENDED':: character varying,
        'INACTIVE':: character varying])::text[])
) ,
    updated_by      varchar(255)
);

create table jr_users
(
    deleted    boolean      not null,
    enabled    boolean,
    created_at timestamp(6) not null,
    updated_at timestamp(6),
    created_by varchar(255) not null,
    email      varchar(255),
    first_name varchar(255),
    id         varchar(255) not null primary key,
    last_name  varchar(255),
    password   varchar(255),
    role       varchar(255)
        constraint jr_users_role_check
            check ((role)::text = ANY
        ((ARRAY [
        'ROLE_PLATFORM_ADMIN':: character varying,
        'ROLE_COMPANY_ADMIN':: character varying,
        'ROLE_ADMINISTRATOR':: character varying,
        'ROLE_SALES_OPERATOR':: character varying,
        'ROLE_USER':: character varying])::text[])
) ,
    tenant_id  varchar(255) constraint fk_user_tenant_id references jr_tenants,
    updated_by varchar(255),
    username   varchar(255)
);

