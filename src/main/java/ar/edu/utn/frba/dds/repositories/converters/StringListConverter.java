package ar.edu.utn.frba.dds.models.repositories.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
//        System.out.println("STRING CONVERTER 1");
//        System.out.println("StringListConverter | convertToDatabaseColumn");
        return attribute != null ? String.join(",", attribute) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
//        System.out.println("STRING CONVERTER 2");
//        System.out.println("StringListConverter | convertToEntityAttribute");
        return dbData != null && !dbData.isEmpty() ? Arrays.stream(dbData.split(",")).collect(Collectors.toList()) : List.of();
    }
}