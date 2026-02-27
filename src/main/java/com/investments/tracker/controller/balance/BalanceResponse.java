package com.investments.tracker.controller.balance;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;


@Schema(description = "The response object for the Balance entity")
public record BalanceResponse (
    @Schema(description = "Date of the balance")
    LocalDate date,

    @Schema(description = "Total balance on the particular date")
    BigDecimal balance,

    @Schema(description = "Total investments till the particular date")
    BigDecimal totalInvestments,

    @Schema(description = "Total deposits till the particular date")
    BigDecimal totalDeposits,

    @Schema(description = "Total withdrawals till the particular date")
    BigDecimal totalWithdrawals,

    @Schema(description = "Total dividends received till the particular date")
    BigDecimal totalDividends,

    @Schema(description = "Total fees paid till the particular date")
    BigDecimal totalFees,

    @Schema(description = "The last portfolio value on the particular date")
    BigDecimal lastPortfolioValue) {}
