package dev.jramde.saas.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static dev.jramde.saas.common.JrUtils.resolveSchemaName;

@Component
@RequiredArgsConstructor
@Slf4j
public class JrTenantSchemaResolver {
    private static final String PUBLIC_SCHEMA = "public";
    private final JdbcTemplate jdbcTemplate;

    /**
     * Resolves the database schema name for a given tenant ID.
     * If the tenant ID is null, or if no active schema is found for the given tenant,
     * the default "public" schema is returned.
     * The resolution process involves querying the database to retrieve the associated company code,
     * which is then transformed into a schema name.
     *
     * @param tenantId the unique identifier of the tenant for which the schema needs to be resolved
     * @return the resolved schema name for the tenant, or the default "public" schema if no specific schema is found
     */
    @Cacheable(value = "tenantSchemas", key = "#tenantId")
    public String resolveSchema(final String tenantId) {

        if (tenantId == null) {
            return PUBLIC_SCHEMA;
        }

        try {
            final String companyCode = jdbcTemplate.queryForObject(
                    "SELECT company_code FROM public.jr_tenants "
                            + "WHERE id = ? AND status = 'ACTIVE' AND deleted = false",
                    String.class,
                    tenantId
            );
            if (StringUtils.hasText(companyCode)) {
                final String schemaName = resolveSchemaName(companyCode);
                log.debug("Resolved schema name: {} for tenant: {}", schemaName, companyCode);
                return schemaName;
            } else {
                log.warn("Schema not found for tenant: {}", tenantId);
                return PUBLIC_SCHEMA;
            }
        } catch (final Exception e) {
            log.error("Error resolving schema for tenant: {}", tenantId, e);
            return PUBLIC_SCHEMA;
        }
    }
}
