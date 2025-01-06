package com.inghub.loan_api.utils;

import com.inghub.loan_api.models.enums.NumberOfInstallments;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NumberOfInstallmentsConverter implements AttributeConverter<NumberOfInstallments, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NumberOfInstallments attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public NumberOfInstallments convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return NumberOfInstallments.fromValue(dbData);
    }
}
