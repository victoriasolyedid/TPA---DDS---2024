package ar.edu.utn.frba.dds.models.repositories.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Converter(autoApply = false)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
        //System.out.println("LOCAL DATE TIME CONVERTER 1");
        //System.out.println("LocalDateTimeAttributeConverter | convertToDatabaseColumn");

        return (locDateTime == null ? null : Timestamp.valueOf(locDateTime));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        //System.out.println("LOCAL DATE TIME CONVERTER 2");
        //System.out.println("LocalDateTimeAttributeConverter | convertToEntityAttribute");

        return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
    }
}