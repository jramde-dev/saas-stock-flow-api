package dev.jramde.saas.config.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER;

/**
 * Implementation of a multi-tenant connection provider for Hibernate.
 * This class manages database connections for different tenants in a multi-tenancy architecture,
 * ensuring that each tenant has its own database schema.
 * <p>
 * The main responsibilities of this class include:
 * - Providing a connection to any tenant or the default schema.
 * - Configuring the database search path to isolate tenant-specific data.
 * - Releasing connections and resetting the search path after use.
 * - Integrating with Hibernate's multi-tenancy configuration as a MultiTenantConnectionProvider and
 * HibernatePropertiesCustomizer.
 * <p>
 * Key Concepts:
 * - Tenants are isolated using PostgreSQL schemas.
 * - The `search_path` in PostgreSQL is dynamically updated to switch between tenant schemas.
 * - The default schema is defined as "public".
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JrMultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider<String>,
        HibernatePropertiesCustomizer {

    private static final String PUBLIC_SCHEMA = "public";
    private final DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(final Connection connection) throws SQLException {
        connection.close();
    }

    /**
     * Retrieves a database connection for the specified tenant.
     * If a tenant identifier is provided and is not equal to the public schema identifier,
     * this method sets the search path of the connection to the specified tenant schema
     * while ensuring the public schema remains accessible.
     *
     * @param tenantIdentifier the unique identifier of the tenant for which the database connection
     *                         is required. If null or equal to the public schema identifier, no schema
     *                         switching is performed.
     * @return a {@code Connection} object configured for the specified tenant.
     * @throws SQLException if there is an error during the schema switching or connection acquisition process.
     */
    @Override
    public Connection getConnection(final String tenantIdentifier) throws SQLException {
        // Récupérer la connexion vers la db
        log.debug("Getting connection for tenant: {}", tenantIdentifier);
        final Connection connection = getAnyConnection();

        try {
            if (tenantIdentifier != null && !tenantIdentifier.equals(PUBLIC_SCHEMA)) {
                connection.createStatement().execute("SET search_path TO " + tenantIdentifier + ", public");
                log.trace("Set search_path to {}", tenantIdentifier);
            }
        } catch (final SQLException e) {
            log.error("Error getting connection for tenant: {}", tenantIdentifier, e);
            throw e;
        }

        return connection;
    }

    @Override
    public void releaseConnection(final String tenantIdentifier, final Connection connection) throws SQLException {
        try {
            connection.createStatement().execute("SET search_path TO public");
        } catch (final SQLException e) {
            log.error("Error releasing connection for tenant: {}", tenantIdentifier, e);
        }
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

    /**
     * Customizes the Hibernate properties for multi-tenant configuration by setting the
     * {@code MULTI_TENANT_CONNECTION_PROVIDER} property to this instance.
     * This method ensures that the application is properly configured to utilize a
     * connection provider that supports multi-tenancy.
     *
     * @param hibernateProperties a map of Hibernate properties to be customized.
     *                            This map is used to integrate the multi-tenant connection
     *                            provider into the Hibernate environment.
     */
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MULTI_TENANT_CONNECTION_PROVIDER, this);
    }
}
