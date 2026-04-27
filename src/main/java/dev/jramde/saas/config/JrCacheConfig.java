package dev.jramde.saas.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration pour activer le caching en mémoire.
 */
@Configuration
@EnableCaching
public class JrCacheConfig {

    /**
     * Configures and provides a CacheManager bean, which is responsible for managing in-memory caching.
     * The caching mechanism uses a concurrent map to store cached data.
     *
     * @return an instance of ConcurrentMapCacheManager configured with a single cache named "tenantSchemas".
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("tenantSchemas");
    }
}
