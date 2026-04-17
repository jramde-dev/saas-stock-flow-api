package dev.jramde.saas.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Classe Java qui va intercepter les requêtes HTTP entrantes
 * pour extraire le tenantId.
 * C'est le point d'entrée du mécanisme multi-tenant, s'exécutant avant tous les controllers et services.
 * <p>
 * Stratégie d'identification du tenant (par ordre de priorité):
 * <p> 1. Header HTTP "X-Tenant-ID"
 * <p> 2. (Optionnel) Sous-domaine : pikachu.stockflow.com -> "pikachu"
 *
 * <p>
 * Si aucun tenant n'est identifié, une erreur 400 (Bad Request) est retournée.
 * </p>
 */
// @Component
// @Order(Ordered.HIGHEST_PRECEDENCE)
public class JrTenantFilter implements Filter {
    private static final String TENANT_HEADER = "X-Tenant-ID";

    @Override
    public void doFilter(
            final ServletRequest servletRequest,
            final ServletResponse servletResponse,
            final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        final String tenantId = resolveTenant(httpRequest);
        if (tenantId == null || tenantId.isBlank()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"Tenant ID is missing in the request header. "
                    + "Please add the header 'X-Tenant-ID' with a valid Tenant ID.\"}");
            return;
        }
        try {
            // Stocker le tenantId dans le thread local
            JrTenantContext.setCurrentTenant(tenantId);

            // Continuer la chaîne de filtres: controller -> service -> repository
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // CRITIQUE: Toujour nettoyer le thread local après la requête.
            // Sans ce clear(), le tenant pourrait fuiter dans la requête suivante
            // si le thread est réutilisé par le pool de threads du serveur.
            JrTenantContext.clear();
        }
    }

    private String resolveTenant(HttpServletRequest httpRequest) {
        final String tenantId = httpRequest.getHeader(TENANT_HEADER);
        if (tenantId != null && !tenantId.isBlank()) {
            return tenantId.trim().toLowerCase();
        }
        return null;
    }
}
