package com.investments.tracker.unit.service.deposit;

import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.DepositMapper;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.controller.deposit.DepositRequest;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.deposit.DepositBalanceBuilderService;
import com.investments.tracker.service.deposit.DepositService;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static com.investments.tracker.enums.Currency.EUR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepositServiceTest {

    @InjectMocks
    private DepositService depositService;

    @Mock
    private DepositBalanceBuilderService depositBalanceBuilderService;

    @Mock
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private CashTransactionMapper cashTransactionMapper;

    @Mock
    private DepositMapper depositMapper;

    private DepositRequest depositRequest;
    private CashTransaction deposit;
    private Balance balance;
    private Balance balance2;
    private final LocalDate DATE = LocalDate.of(2025, 1, 1);

    @BeforeEach
    void setUp() {
        depositRequest = DepositRequest.builder()
                .date(DATE)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        deposit = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(DEPOSIT)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("TEST DESCRIPTION")
                .build();

        balance = Balance.builder()
                .date(DATE)
                .balance(BigDecimal.valueOf(1000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.valueOf(1000))
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();

        balance2 = Balance.builder()
                .date(DATE)
                .balance(BigDecimal.valueOf(2000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.valueOf(2000))
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
    }

    @Test
    @DisplayName("Test should insert a successful deposit for the first time")
    void testInsertSuccessfulDepositForTheFirstTime() {
        when(cashTransactionMapper.createCashtransaction(eq(depositRequest), eq(depositMapper))).thenReturn(deposit);
        when(cashTransactionRepository.save(any(CashTransaction.class))).thenReturn(deposit);
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());
        when(depositBalanceBuilderService.createBalanceFromCashTransaction(isNull(), eq(deposit))).thenReturn(balance);

        BalanceResponse balanceResponse = depositService.insertDeposit(depositRequest);
        assertEquals(balanceResponse.balance(), BigDecimal.valueOf(1000));
        assertEquals(balanceResponse.totalDeposits(), BigDecimal.valueOf(1000));

        verify(cashTransactionMapper).createCashtransaction(eq(depositRequest), eq(depositMapper));
        verify(cashTransactionRepository).save(any(CashTransaction.class));
        verify(balanceRepository).findTopByOrderByIdDesc();
        verify(depositBalanceBuilderService).createBalanceFromCashTransaction(isNull(), eq(deposit));
        verify(balanceRepository).save(any(Balance.class));
    }

    @Test
    @DisplayName("Test should create a successful deposit")
    void testInsertSuccessfulDeposit() {
        when(cashTransactionMapper.createCashtransaction(eq(depositRequest), eq(depositMapper))).thenReturn(deposit);
        when(cashTransactionRepository.save(any(CashTransaction.class))).thenReturn(deposit);
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(balance));
        when(depositBalanceBuilderService.createBalanceFromCashTransaction(eq(balance), eq(deposit))).thenReturn(balance2);

        BalanceResponse balanceResponse = depositService.insertDeposit(depositRequest);
        assertEquals(0, balanceResponse.balance().compareTo(BigDecimal.valueOf(2000)));
        assertEquals(0, balanceResponse.totalDeposits().compareTo(BigDecimal.valueOf(2000)));

        verify(cashTransactionMapper).createCashtransaction(eq(depositRequest), eq(depositMapper));
        verify(cashTransactionRepository).save(any(CashTransaction.class));
        verify(balanceRepository).findTopByOrderByIdDesc();
        verify(depositBalanceBuilderService).createBalanceFromCashTransaction(eq(balance), eq(deposit));
        verify(balanceRepository).save(any(Balance.class));
    }

}
