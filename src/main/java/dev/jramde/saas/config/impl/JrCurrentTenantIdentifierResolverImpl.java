package dev.jramde.saas.config.impl;

import dev.jramde.saas.config.JrTenantContext;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER;

/**
 * Implementation of the interfaces for multi-tenancy.
 * This class is responsible for determining the current tenant identifier and integrating it into the
 * Hibernate multi-tenancy configuration.
 * <p>
 * The current tenant identifier is retrieved from the {@link JrTenantContext}, which manages tenant-specific
 * context for each thread using a {@link ThreadLocal} mechanism. It ensures thread isolation and tenant-specific
 * data segregation.
 * <p>
 * The class also customizes Hibernate properties to integrate the tenant identifier resolver into
 * the Hibernate environment. Additionally, it validates the existence of active sessions for the current tenant
 * context when needed.
 */
@Component
@Slf4j
public class JrCurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String>,
        HibernatePropertiesCustomizer {

    /**
     * Récupère l'identifiant du tenant actuel utilisé pour déterminer le contexte
     * du tenant actif dans une configuration d'application multi-tenants. Cette méthode est appelée
     * pendant le processus de détermination du tenant afin d'obtenir l'identifiant du tenant
     * actuellement concerné.
     *
     * @return l'identifiant du tenant sous forme de chaîne de caractères, ou une chaîne vide/un espace réservé si
     * le tenant ne peut pas être identifié.
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        final String schemaName = JrTenantContext.getCurrentSchema();
        log.trace("|> Current tenant schema identifier : {}", schemaName);
        return schemaName;
    }

    /**
     * Validates the existing database sessions for the current tenant context.
     * This method is invoked to determine if the established sessions
     * are still valid for continued operations.
     *
     * @return false if the existing sessions are considered invalid, true otherwise.
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    /**
     * Customizes the Hibernate properties by adding the current tenant identifier resolver.
     * This is used to configure the Hibernate properties for the application context.
     *
     * @param hibernateProperties the map of Hibernate properties to customize
     */
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
