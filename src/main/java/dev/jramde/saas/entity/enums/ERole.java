package dev.jramde.saas.entity.enums;

public enum ERole {
    ROLE_PLATFORM_ADMIN, // Super Admin, le propriétaire de l'application
    ROLE_COMPANY_ADMIN,
    ROLE_ADMINISTRATOR, // Tenant admin, propriétaire d'une entreprise
    ROLE_SALES_OPERATOR,
    ROLE_USER,
}
