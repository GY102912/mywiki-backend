package com.jian.community.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jian.community.domain.exception.DataConversionException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) return "[]";

        try {
            return mapper.writeValueAsString(stringList);

        } catch (JsonProcessingException e) {
            throw new DataConversionException();
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        if (s == null || s.isBlank()) return new ArrayList<>();

        try {
            JavaType valueType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
            return mapper.readValue(s, valueType);

        } catch (JsonProcessingException e) {
            throw new DataConversionException();
        }
    }
}
