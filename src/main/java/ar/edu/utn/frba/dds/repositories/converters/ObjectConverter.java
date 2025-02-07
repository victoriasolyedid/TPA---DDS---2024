package ar.edu.utn.frba.dds.models.repositories.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class ObjectConverter implements AttributeConverter<Object, String> {

    private final ObjectMapper objectMapper = ObjectMapperConfig.createObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        //System.out.println("OBJECT CONVERTER 1");
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting object to JSON string", e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        //System.out.println("OBJECT CONVERTER 2");
        try {
            return objectMapper.readValue(dbData, Object.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON string to object", e);
        }
    }
}