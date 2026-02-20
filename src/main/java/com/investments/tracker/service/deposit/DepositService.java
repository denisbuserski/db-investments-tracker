package com.investments.tracker.service.deposit;

import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.controller.deposit.DepositRequest;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.DepositMapper;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.investments.tracker.controller.balance.BalanceResponse.createBalanceResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepositService {
    private final CashTransactionRepository cashTransactionRepository;
    private final BalanceRepository balanceRepository;
    private final CashTransactionMapper cashTransactionMapper;
    private final DepositMapper depositMapper;
    private final DepositBalanceBuilderService depositBalanceBuilderService;

    @Transactional
    public BalanceResponse insertDeposit(DepositRequest depositRequest) {
        CashTransaction deposit = cashTransactionMapper.createCashtransaction(depositRequest, depositMapper);
        cashTransactionRepository.save(deposit);
        log.info("Deposit successfully saved in the database");
        Balance newBalance;

        Optional<Balance> latestBalance = balanceRepository.findTopByOrderByIdDesc();
        if (latestBalance.isPresent()) {
            newBalance = depositBalanceBuilderService.createBalanceFromCashTransaction(latestBalance.get(), deposit);
        } else {
            newBalance = depositBalanceBuilderService.createBalanceFromCashTransaction(null, deposit);
        }
        balanceRepository.save(newBalance);
        log.info("Balance successfully saved in the database");
        log.info("Deposit for [{} {}] successfully inserted", String.format("%.2f", deposit.getAmount()), deposit.getCurrency());
        return createBalanceResponse(newBalance);
    }

}
