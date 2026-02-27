package com.investments.tracker.service.withdrawal;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.service.BalanceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
public class WithdrawalBalanceBuilderService extends BalanceBuilder {
    @Override
    public Balance createBalanceFromCashTransaction(Balance balance, CashTransaction withdrawal) {
        LocalDate newBalanceDate = withdrawal.getDate();
        BigDecimal newBalanceAmount = balance.getBalance().subtract(withdrawal.getAmount());
        BigDecimal newTotalWithdrawals = balance.getTotalWithdrawals().add(withdrawal.getAmount());

        return balanceBuilder(
                newBalanceDate,
                newBalanceAmount,
                balance.getTotalInvestments(),
                balance.getTotalDeposits(),
                newTotalWithdrawals,
                balance.getTotalDividends(),
                balance.getTotalFees(),
                balance.getLastPortfolioValue(),
                balance.getLastUnrealizedPl(),
                balance.getLastUnrealizedPlPercentage(),
                balance.getTotalSold(),
                balance.getRealizedPl());
    }
}
