package com.trier.trier_report.security;

import com.trier.trier_report.entity.base.Ownable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("dbSecurity")
public class GenericSecurity {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean isOwner(Long id, String entityClassName, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        try {
            Class<?> entityClass = Class.forName("com.trier.trier_report.entity" + entityClassName);
            Object entity = entityManager.find(entityClass, id);

            if (entity instanceof Ownable ownableEntity) {
                String currentUserIdStr = auth.getName();
                Long currentUserId = Long.valueOf(currentUserIdStr);

                return ownableEntity.getUserId().equals(currentUserId);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
