package com.investments.tracker.service;

import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.dividend.DividendRequest;
import com.investments.tracker.enums.CashTransactionType;
import com.investments.tracker.repository.CashTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.CashTransactionType.DIVIDEND;
import static com.investments.tracker.enums.Currency.EUR;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashTransactionService {
    private final CashTransactionRepository cashTransactionRepository;
    private final CashTransactionMapper cashTransactionMapper;

    public List<CashTransactionResponse> getAllCashTransactionsFromTo(CashTransactionType cashTransactionType, LocalDate from, LocalDate to) {
        log.info("Getting all {} from {} to {}", cashTransactionType.name().toUpperCase(), from, to);
        List<CashTransaction> cashTransactions = cashTransactionRepository.findByCashTransactionTypeAndDateBetween(cashTransactionType, from, to);

        if (!cashTransactions.isEmpty()) {
            log.info("Found {} cash transactions of type {}", cashTransactions.size(), cashTransactionType.name().toUpperCase());
            return cashTransactionMapper.mapToResponseDTOList(cashTransactions, DEPOSIT);
        }
        log.info("No cash transaction of type {} found", cashTransactionType.name().toUpperCase());
        return Collections.emptyList();
    }

    public BigDecimal getTotalAmount(CashTransactionType cashTransactionType) {
        log.info("Getting total amount of {}", cashTransactionType.name().toUpperCase());
        return cashTransactionRepository.getTotalAmountOf(cashTransactionType).orElse(BigDecimal.ZERO);
    }

    public CashTransaction createCashTransactionForDividend(DividendRequest dividendRequest, BigDecimal dividendAmount) {
        String dividendDescription = String.format("Dividend for product [%s]", dividendRequest.getProductName());

        return CashTransaction.builder()
                .date(dividendRequest.getDate())
                .cashTransactionType(DIVIDEND)
                .amount(dividendAmount)
                .currency(EUR)
                .description(dividendDescription)
                .referenceId(null)
                .build();
    }

    public CashTransaction createCashTransactionForFee(LocalDate date, CashTransactionType cashTransactionType, String feeType, BigDecimal feeValue, long transactionId) {
        String feeDescription = String.format("Fee of type %s; Reference id to 'transaction' table", feeType);

        return CashTransaction.builder()
                .date(date)
                .cashTransactionType(cashTransactionType)
                .amount(feeValue)
                .currency(EUR)
                .description(feeDescription)
                .referenceId(transactionId)
                .build();
    }


}
