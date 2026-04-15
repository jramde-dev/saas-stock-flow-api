package dev.jramde.saas.entity.enums;

public enum ERole {
    PLATFORM_ADMIN, // Super Admin, le propriétaire de l'application
    COMPANY_ADMIN,
    ADMINISTRATOR, // Tenant admin, propriétaire d'une entreprise
    SALES_OPERATOR,
    USER,
}
