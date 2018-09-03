package com.mackleaps.formium.model.util;

/**
 * Class responsible for retrieving an entity from the database, based on its type
 * Returned objects should not be attached to a EntityManager
 * */
public interface EntityFromDatabaseManager {

    <T,I> T find(Class<T> clazz, I id);

}
