package ar.edu.utn.frba.dds.models.repositories.converters;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class OptionalStringAttributeConverter implements AttributeConverter<Optional<String>, String> {

    @Override
    public String convertToDatabaseColumn(Optional<String> attribute) {
//        System.out.println("OPTIONAL CONVERTER 1");
//        System.out.println("OptionalStringAttributeConverter | convertToDatabaseColumn");
        return attribute.orElse(null);
    }

    @Override
    public Optional<String> convertToEntityAttribute(String dbData) {
//        System.out.println("OPTIONAL CONVERTER 2");
//        System.out.println("OptionalStringAttributeConverter | convertToEntityAttribute");
        return Optional.ofNullable(dbData);
    }
}