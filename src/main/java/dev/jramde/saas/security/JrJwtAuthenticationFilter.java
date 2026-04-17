package dev.jramde.saas.security;

import dev.jramde.saas.config.JrTenantContext;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtre d'authentification JWT.
 * Toute requête passera par ce filtre.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JrJwtAuthenticationFilter extends OncePerRequestFilter {
    private final JrJwtTokenService jwtService;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        // Ne pas vérifier le token pour la route de login
        if (request.getRequestURI().contains("/api/v1/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Filtrer les informations du token
        try {
            final String jwtToken = getJwtTokenFromRequest(request);
            if (StringUtils.hasText(jwtToken) && jwtService.validateToken(jwtToken)) {
                final String userId = jwtService.getUserIdFromToken(jwtToken);
                final String tenantId = jwtService.getTenantIdFromToken(jwtToken);
                final String role = jwtService.getRoleFromToken(jwtToken);

                if (tenantId != null) {
                    JrTenantContext.setCurrentTenant(tenantId);
                    final String schemaName = "to-be-defined";
                    JrTenantContext.setCurrentSchema(schemaName);
                }

                // Create authentication token
                final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, Collections.singletonList(authority));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Successfully authenticated for user: {}, tenant: {}, role: {}", userId, tenantId, role);
            }
        } catch (final Exception e) {
            log.error("Error authenticating user.", e);
        }
        filterChain.doFilter(request, response);
        JrTenantContext.clear();
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}











