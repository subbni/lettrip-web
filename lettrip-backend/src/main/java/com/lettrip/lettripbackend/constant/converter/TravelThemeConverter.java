package com.lettrip.lettripbackend.constant.converter;

import com.lettrip.lettripbackend.constant.TravelTheme;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TravelThemeConverter implements AttributeConverter<TravelTheme,String> {
    @Override
    public String convertToDatabaseColumn(TravelTheme travelTheme) {
        return travelTheme.getKoreanName();
    }

    @Override
    public TravelTheme convertToEntityAttribute(String dbData) {
        return TravelTheme.of(dbData);
    }
}
