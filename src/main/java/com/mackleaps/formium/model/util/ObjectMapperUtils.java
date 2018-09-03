package com.mackleaps.formium.model.util;

import com.mackleaps.formium.model.dto.DTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Util for conversion from DTOs to Entities and from Entities to DTOs
 * */
public class ObjectMapperUtils {

    private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    /**
     * Dont allow public usage
     * */
    private ObjectMapperUtils () { }

    public static <D,T> D map (final T resource, Class<D> destinationClass) {
        return modelMapper.map(resource, destinationClass);
    }

    public static <D,T> D map(final T resource, D destinationObject) {
        modelMapper.map(resource, destinationObject);
        return destinationObject;
    }

    public static <D,T> List<D> mapAll(final Collection <T> entityList, Class<D> resultClass) {
        return entityList.stream()
                .map(entity -> map(entity, resultClass))
                .collect(Collectors.toList());
    }

    public static <T,D extends DTO<T,D>> D mapFromDto(T entity, Class<D> clazz) {
        try {
            return clazz.newInstance().convertEntityToDto(entity,clazz);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

}
