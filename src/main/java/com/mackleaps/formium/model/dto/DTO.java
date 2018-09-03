package com.mackleaps.formium.model.dto;

import com.mackleaps.formium.model.util.ObjectMapperUtils;

public abstract class DTO<T,D extends DTO<T,D>> {

	/**
	 * @param existingEntity with the default values to be conserved in case fields not found on dto
	 * @return a converted entity from the values of current dto
	 * */
    public T convertToExistingEntity (T existingEntity) {
        ObjectMapperUtils.map(this, existingEntity);
        return existingEntity;
    }

    /**
     * @param classType from which the entity will be created
     * @return a entity with the values filled with fields from current dto
     * */
    public T convertToNonExistingEntity (Class<T> classType) {
        return ObjectMapperUtils.map(this, classType);
    }

    /**
     * @param entity from which the dto will be converted
     * @param dtoClass to be created
     * @return a dto with the field filled with entity class values
     * */
    public D convertEntityToDto(T entity, Class<D> dtoClass){
        return ObjectMapperUtils.map(entity, dtoClass);
    }

}
