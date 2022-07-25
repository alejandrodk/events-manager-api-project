package com.events.api.domain.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public enum ModelMapperUtils {
    INSTANCE;
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <T> T mapToClass(Object object, Class<T> clazz) {
        return modelMapper.map(object, clazz);
    }

    public static <T> T mapToClassStrict(Object object, Class<T> clazz) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        T result = modelMapper.map(object, clazz);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return result;
    }
}
