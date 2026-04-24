package dev.jramde.saas.security;

import dev.jramde.saas.config.properties.JrJwtProperties;
import dev.jramde.saas.exception.JrUnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JrJwtTokenService {
    private final JrJwtProperties jwtProperties;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    /**
     * Charger les clés privées et publiques dans la mémoire.
     * S'exécute de facon automatique une fois que la classe est initialisée.
     */
    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey(jwtProperties.getPrivateKeyPath());
            this.publicKey = loadPublicKey(jwtProperties.getPublicKeyPath());

            log.info("Private and public keys loaded successfully");
        } catch (final Exception e) {
            log.error("Failed to load private or public key", e);
            throw new RuntimeException("Failed to load private or public key", e);
        }
    }

    /**
     * Génère un token JWT d'authentification.
     *
     * @param tenantId
     * @param userId
     * @param role
     * @return
     */
    public String generateAccessToken(
            @Nonnull final String tenantId,
            @Nonnull final String userId,
            @Nonnull final String role) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + this.jwtProperties.getAccessTokenExpiration());
        return Jwts.builder()
                .subject(userId)
                .claim("tenantId", tenantId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .issuer("saas-stock-flow")
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public String getUserIdFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public String getTenantIdFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("tenantId", String.class);
    }

    public String getRoleFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    /**
     * Vérifie si le token est valide.
     *
     * @param token : le token à vérifier.
     * @return true si le token est valide ou lève une exception sinon.
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(this.publicKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (final ExpiredJwtException e) {
            throw new JrUnauthorizedException("Token has expired.");
        } catch (final UnsupportedJwtException e) {
            throw new JrUnauthorizedException("Token is not signed.");
        } catch (final MalformedJwtException e) {
            throw new JrUnauthorizedException("Token is malformed.");
        } catch (final SecurityException e) {
            throw new JrUnauthorizedException("Invalid JWT Signature.");
        } catch (final IllegalArgumentException e) {
            throw new JrUnauthorizedException("Invalid JWT Claims.");
        }
    }

    /**
     * Récupère les claims du token après vérification.
     *
     * @param token
     * @return les claims du token.
     */
    private Claims getClaimsFromToken(final String token) {
        return Jwts.parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Lire la clé privée.
     *
     * @param privateKeyPath
     * @return
     * @throws Exception
     */
    private PrivateKey loadPrivateKey(final String privateKeyPath) throws Exception {
        try (final InputStream inputStream = JrJwtTokenService.class.getResourceAsStream(privateKeyPath)) {
            if (inputStream == null) {
                throw new RuntimeException("Private key not found.");
            }

            final String key = new String(inputStream.readAllBytes());
            final String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        }
    }

    /**
     * Lire la clé publique.
     *
     * @param publicKeyPath : le chemin de la clé publique
     * @return la clé publique
     * @throws Exception
     */
    private PublicKey loadPublicKey(final String publicKeyPath) throws Exception {
        try (final InputStream inputStream = JrJwtTokenService.class.getResourceAsStream(publicKeyPath)) {
            if (inputStream == null) {
                throw new RuntimeException("Public key not found.");
            }

            final String key = new String(inputStream.readAllBytes());
            final String publicKeyPEM = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            final byte[] decodedKey = Base64.getDecoder().decode(publicKeyPEM);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        }
    }
}
