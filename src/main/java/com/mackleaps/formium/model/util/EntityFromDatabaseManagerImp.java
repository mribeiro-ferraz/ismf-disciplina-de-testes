package com.mackleaps.formium.model.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
public class EntityFromDatabaseManagerImp implements EntityFromDatabaseManager {

    private EntityManager entityManager;

    @Autowired
    public EntityFromDatabaseManagerImp (EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public <T, I> T find(Class<T> clazz, I id) {

        T entity = entityManager.find(clazz,id);
        entityManager.detach(entity);
        return entity;
    }
}
