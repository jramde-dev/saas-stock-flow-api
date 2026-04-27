package dev.jramde.saas.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

/**
 * Une classe qui intègre le tenantFilter d'Hibernate avec le contexte du locataire courant.
 * Cet aspect garantit que le tenantFilter approprié est activé avant toute exécution de méthode de repository.
 * Le tenantFilter est configuré avec l'ID du locataire extrait du `JrTenantContext` lié au thread.
 * Cela permet l'isolation des requêtes par locataire dans une application multi-locataire.
 *
 * <p>
 * Responsabilités :
 * <p>- Extraire l'ID du locataire courant depuis `JrTenantContext`.
 * <p>- Activer le tenantFilter Hibernate pour toute méthode de repository dans le package spécifié.
 * </p>
 *
 * <p>
 * Détails de conception :
 * <p>- L'annotation `@Aspect` marque cette classe comme un Aspect, définissant un comportement transversal
 * pour les méthodes du package repository.
 * <p>- L'annotation `@Before` garantit que le tenantFilter est activé avant l'exécution de la méthode.
 * <p>- L'`EntityManager` est déballé en une `Session` Hibernate afin de permettre un filtrage personnalisé.
 *
 * <p>
 * Préconditions :
 * <p>- Un filtre Hibernate nommé "tenantFilter" doit être configuré dans les modèles d'entités de l'application.
 * <p>- Le `JrTenantContext` doit être correctement initialisé avec un ID de locataire avant l'exécution de la méthode
 * repository.
 *
 * <p>
 * Limitations :
 * <p>- Si aucun ID de locataire n'est présent dans `JrTenantContext`, le tenantFilter ne sera pas activé,
 * ce qui entraînera l'exécution des requêtes sans isolation par locataire.
 * <p>- Un nettoyage correct de l'ID du locataire stocké dans le thread-local via `JrTenantContext.clear()`
 * est essentiel pour éviter les fuites de données entre les requêtes.
 *
 * <p>
 * POURQUOI UN ASPECT ?
 * <p> Sans l'aspect, il faudrait activer manuellement le filtre dans chaque méthode du service.
 * L'aspect le fait automatiquement et de manière transversale.
 *
 * <p>
 * ALTERNATIVE :
 * <p> On pourrait aussi utiliser un HandlerInterceptor ou un @EventListener.
 * L'aspect est plus propre car il s'exécute au plus proche de la couche de données.
 */
// @Aspect
// @Component
public class JrTenantHibernateFilterAspect {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Active le tenantHibernateFilter avant l'exécution de la méthode de repository.
     * Le filtre est configuré avec le tenantId extrait du `JrTenantContext` lié au thread.
     */
    @Before("execution(* dev.jramde.saas.repository.*.*(..))")
    public void activateTenantFilter() {
        final String tenantId = JrTenantContext.getCurrentTenant();

        if (tenantId != null) {
            final Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter")
                    .setParameter("tenantId", tenantId);
        }
    }
}
