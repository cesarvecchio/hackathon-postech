package br.fiap.hackathonpostech.domain.enums.converters;

import br.fiap.hackathonpostech.domain.enums.MetodoPagamentoEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MetodoPagamentoEnumConverter implements AttributeConverter<MetodoPagamentoEnum, String> {

    @Override
    public String convertToDatabaseColumn(MetodoPagamentoEnum attribute) {
        return attribute != null ? attribute.getValor() : null;
    }

    @Override
    public MetodoPagamentoEnum convertToEntityAttribute(String dbData) {
        return dbData != null ? MetodoPagamentoEnum.fromValor(dbData) : null;
    }
}
