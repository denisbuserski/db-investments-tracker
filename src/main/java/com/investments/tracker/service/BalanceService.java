package com.investments.tracker.service;

import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.model.Balance;
import com.investments.tracker.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public BalanceResponse getLatestBalanceData(LocalDateTime dateTime) {
        Optional<Balance> latestBalance = this.balanceRepository.findTopByOrderByIdDesc();
        if (latestBalance.isPresent()) {
            return createBalanceResponse(latestBalance.get());
        }
        log.info("No balance found for [{}]", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        return createBalanceResponse(null);
    }

    public static BalanceResponse createBalanceResponse(Balance newBalance) {
        LocalDate newBalanceDate = newBalance == null ? LocalDate.now() : newBalance.getDate();
        BigDecimal newBalanceAmount = newBalance == null ? BigDecimal.ZERO : newBalance.getBalance();
        BigDecimal newTotalInvestments = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalInvestments();
        BigDecimal newTotalDeposits = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalDeposits();
        BigDecimal newTotalWithdrawals = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalWithdrawals();
        BigDecimal newTotalDividends = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalDividends();
        BigDecimal newTotalFees = newBalance == null ? BigDecimal.ZERO : newBalance.getTotalFees();
        BigDecimal newLastPortfolioValue = newBalance == null ? BigDecimal.ZERO : newBalance.getLastPortfolioValue();

        return new BalanceResponse(
                newBalanceDate,
                newBalanceAmount,
                newTotalInvestments,
                newTotalDeposits,
                newTotalWithdrawals,
                newTotalDividends,
                newTotalFees,
                newLastPortfolioValue);
    }


}
