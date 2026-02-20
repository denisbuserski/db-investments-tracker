package com.investments.tracker.validation;

import com.investments.tracker.enums.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, Currency> {
    @Override
    public boolean isValid(Currency currency, ConstraintValidatorContext context) {
        if (currency == null) {
            // This is handled by '@NotNull(message = CURRENCY_NOT_NULL)'
            return true;
        }
        return Arrays.asList(Currency.values()).contains(currency);
    }
}
