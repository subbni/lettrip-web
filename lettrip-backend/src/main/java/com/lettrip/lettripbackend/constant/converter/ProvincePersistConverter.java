package com.lettrip.lettripbackend.constant.converter;

import com.lettrip.lettripbackend.constant.Province;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProvincePersistConverter implements AttributeConverter<Province,String> {

    @Override
    public String convertToDatabaseColumn(Province province) {
        return province.getKoreanName();
    }

    @Override
    public Province convertToEntityAttribute(String dbData) {
        return Province.of(dbData);
    }
}
