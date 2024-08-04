package br.fiap.hackathonpostech.infra.persistence.entity.enums.converters;

import br.fiap.hackathonpostech.domain.enums.StatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusEnumConverter implements AttributeConverter<StatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(StatusEnum attribute) {
        return attribute != null ? attribute.getValor() : null;
    }

    @Override
    public StatusEnum convertToEntityAttribute(String dbData) {
        return dbData != null ? StatusEnum.fromValor(dbData) : null;
    }
}
