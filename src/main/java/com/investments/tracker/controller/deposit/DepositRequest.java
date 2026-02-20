package com.investments.tracker.controller.deposit;

import com.investments.tracker.enums.Currency;
import com.investments.tracker.validation.ValidCurrency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.investments.tracker.validation.ValidationMessages.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Schema(description = "The request object for inserting a deposit")
public class DepositRequest {
    @NotNull(message = DATE_NOT_NULL_NOR_EMPTY)
    @PastOrPresent(message = DATE_NOT_IN_FUTURE)
    @Schema(description = "Deposit date", example = "2025-01-15")
    private LocalDate date;

    @NotNull(message = AMOUNT_NOT_NULL)
    @Positive(message = AMOUNT_MORE_THAN_ZERO)
    @Digits(integer = 10, fraction = 2, message = AMOUNT_DIGITS)
    @Schema(description = "Deposit amount", example = "1500.35")
    private BigDecimal amount;

    @NotNull(message = CURRENCY_NOT_NULL)
    @ValidCurrency
    @Schema(description = "Currency of the deposit", example = "EUR")
    private Currency currency;

    @NotNull(message = DESCRIPTION_NOT_NULL)
    @Size(min = 1, max = 255, message = DESCRIPTION_NOT_EMPTY)
    @Schema(description = "A short description of the deposit", example = "January 2025 deposit")
    private String description;
}
