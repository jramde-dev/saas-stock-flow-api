package dev.jramde.saas.service.impl;

import dev.jramde.saas.entity.JrTenant;
import dev.jramde.saas.exception.JrTenantProvisioningException;
import dev.jramde.saas.service.IProvisioningService;
import jakarta.transaction.Transactional;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Cette classe implémente l'interface IProvisioningService pour créer un schema
 * et lancer les migrations pour un nouvean tenant.
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class JrProvisioningServiceImpl implements IProvisioningService {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    final String MIGRATION_LOCATION = "classpath:db/migration/tenant";


    @Override
    public void provideTenant(final JrTenant tenant) {
        final String schemaName = "tenant_" + tenant.getCompanyCode().toLowerCase();

        try {
            log.info("|> Provisioning schema for tenant: {} (schema: {})", tenant.getCompanyName(), schemaName);
            // 1. Create Postgres schema
            createSchema(schemaName);
            log.info("Schema created successfully: {}", schemaName);

            // 2. Run Flyway migrations for this tenant to create initial tables
            log.info("|> Running Flyway migrations for tenant: {} (schema: {})", tenant.getCompanyName(), schemaName);
            runMigrations(schemaName);
            log.info("|> Flyway migrations completed successfully for tenant: {} (schema: {})",
                    tenant.getCompanyName(), schemaName);

            // 3. Initialize tenant default data (optional)
            log.info("|> Initializing default data for tenant: {} (schema: {})",
                    tenant.getCompanyName(), schemaName);
            initializeDefaultData(schemaName, tenant);
            log.info("|> Default data initialized successfully for tenant: {} (schema: {})",
                    tenant.getCompanyName(), schemaName);

        } catch (final Exception e) {
            log.error("|> Failed to provision schema for tenant: {}", tenant.getCompanyName(), e);

            // rollback: drop created schema
            try {
                dropSchema(schemaName);
            } catch (final Exception ex) {
                log.error("|> Failed to rollback schema after provisioning failure: {}", schemaName, ex);
            }

            throw new JrTenantProvisioningException(
                    "Failed to provision schema for tenant: " + tenant.getCompanyName());
        }
    }

    private void createSchema(final String schemaName) {
        final String sql = String.format("CREATE SCHEMA IF NOT EXISTS %s", schemaName);
        jdbcTemplate.execute(sql);
    }

    private void runMigrations(final String schemaName) {
        log.info("|> Running tenant migrations for schema: {}", schemaName);

        final Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schemaName)
                .locations(MIGRATION_LOCATION)
                .baselineOnMigrate(true)
                .table("flyway_schema_history")
                .validateOnMigrate(true)
                .cleanDisabled(true)
                .load();

        log.info("|> Flyway migrations started...");
        flyway.migrate();
        log.info("|> Flyway migrations completed successfully");
    }

    private void initializeDefaultData(final String schemaName, final JrTenant tenant) {
        // Add default data for the tenant
    }

    private void dropSchema(final String schemaName) {
        final String sql = String.format("DROP SCHEMA IF EXISTS %s CASCADE", schemaName);
        jdbcTemplate.execute(sql);
    }
}
