package com.mackleaps.formium.model.util;

import com.mackleaps.formium.model.dto.DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Component
public class PersistedEntitiesObjectMapper {

    private EntityFromDatabaseManager entityManager;

    @Autowired
    public PersistedEntitiesObjectMapper (EntityFromDatabaseManager entityManager){
        this.entityManager = entityManager;
    }

    /**
     * @param entityClass, the Class defining the Entity to which the dto object will be converted to
     * @param dto, the source object to be converted
     * @return the converted object
     * */
    public <T,D extends DTO<T,D>> T convertDTOToEntity (DTO<T,D> dto, Class<T> entityClass) {

        Object id = getEntityId(dto);

        T persistedObject = null;

        if(id != null)
            persistedObject = entityManager.find(entityClass, id);

        if(id == null || persistedObject == null)
            return dto.convertToNonExistingEntity(entityClass);
        else
            return dto.convertToExistingEntity(persistedObject);

    }

    /**
     * @param entityClass, the Class defining the Entity to which the dto object will be converted to
     * @param dtos, the source objects from which the entities will be created
     * @return the converted object
     * */
    public <T,D extends DTO<T,D>> List<T> convertListOfDTOsToEntities(List<D> dtos, Class<T> entityClass) {

        List<T> convertedEntities = new ArrayList<>();
        for(D currentDTO : dtos)
            convertedEntities.add(convertDTOToEntity(currentDTO,entityClass));

        return convertedEntities;
    }

    /**
     * @param entity, the entity from which the dto will be created
     * @param dtoClass, the class from which the new dto object will be created
     * @return a dto object populated with the attributes from the entity class
     * */
    public <T,D extends DTO<T,D>> D convertEntityToDTO (T entity, Class<D> dtoClass) {

        try {
            return dtoClass.newInstance().convertEntityToDto(entity, dtoClass);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Problem trying to instantiate a dto object");
        }
    }

    /**
     * Receive a list of entities and convert those into a list of dtos
     * @param entities from which the new list will be created
     * @param destinationDTO, the type of the list of dtos that will be created
     * @return a new list of dtos out of the entities
     * */
    public <D extends DTO<E,D>, E> List<D> convertListOfEntitiesIntoDtos(List<E> entities, Class<D> destinationDTO) {

        List<D> dtos = new ArrayList<>();
        for(E entity : entities)
            dtos.add(convertEntityToDTO(entity,destinationDTO));

        return dtos;
    }

    /**
     * Search for the id of a given dto object
     *
     * @param dto, object through which fields the id annotation will be looked for
     * @return return id as a object, if it is declared as a field; null otherwise
     * */
    private Object getEntityId(Object dto) {

        for(Field field : dto.getClass().getDeclaredFields()) {
            if(field.getAnnotation(Id.class) != null) {
                try {
                    field.setAccessible(true);
                    return field.get(dto);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException();
                }
            }
        }

        return null;

    }

}
