package com.mackleaps.formium.model.util;

import java.util.List;
import java.util.stream.Collectors;

public class ListUtils {

    /**
     * Get a list of a certain type as cast it to a more specific one
     * @param initialType
     * @param newType
     * @param initialList
     * @return a list casted from the initialType to the newType
     * */
    public static <OLD, NEW extends OLD> List<NEW> convertIntoMoreSpecificTypeOfList(List<OLD> initialList,
                                                                               Class<OLD> initialType,
                                                                               Class<NEW> newType) {
        //i wonder if this is necessary, but just to be safe.
        if(!initialType.isAssignableFrom(newType))
            throw new ClassCastException();

        return initialList.stream().map(newType::cast).collect(Collectors.toList());
    }

}
