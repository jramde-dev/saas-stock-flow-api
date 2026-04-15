package dev.jramde.saas.config;

/**
 * Cette classe JrTenantContext stocke l'identifiant du tenant courant dans un ThreadLocal.
 * Chaque requête est traitée par un thread dédié et distinct.
 * Le ThreadLocal garantit que le tenant est isolé par thread,
 * même en cas de requêtes simultanées provenant de tenants différents.
 * <p>
 * Flux des requêtes HTTP :
 * <p> 1. Un TenantFilter extrait le tenantId de la requête HTTP.
 * <p> 2. Il appelle le TenantContext.setCurrentTenant(tenantId).
 * <p> 3. Le code métier (services, repositories, etc.) récupère le tenantId via le TenantContext.getCurrentTenant().
 * <p> 4. Le TenantFilter appelle le TenantContext.clear() pour nettoyer le ThreadLocal.
 */
public class JrTenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Nettoie le tenant du thread courant.
     * IMPORTANT: Cette méthode doit être appelée à la fin de chaque requête HTTP.
     * ie dans un bloc try-catch-finally pour éviter les fuites de mémoire(memory leak)
     * et les fuites de données entre requêtes.
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
