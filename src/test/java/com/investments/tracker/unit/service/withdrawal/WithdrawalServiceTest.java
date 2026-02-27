package com.investments.tracker.unit.service.withdrawal;

import com.investments.tracker.controller.withdrawal.WithdrawalRequest;
import com.investments.tracker.mapper.CashTransactionMapper;
import com.investments.tracker.mapper.WithdrawalMapper;
import com.investments.tracker.model.Balance;
import com.investments.tracker.model.CashTransaction;
import com.investments.tracker.controller.balance.BalanceResponse;
import com.investments.tracker.repository.BalanceRepository;
import com.investments.tracker.repository.CashTransactionRepository;
import com.investments.tracker.service.withdrawal.WithdrawalBalanceBuilderService;
import com.investments.tracker.service.withdrawal.WithdrawalService;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.investments.tracker.enums.CashTransactionType.WITHDRAWAL;
import static com.investments.tracker.enums.Currency.EUR;
import static com.investments.tracker.validation.ValidationMessages.WITHDRAWAL_DATE_NOT_BEFORE_LATEST_BALANCE;
import static com.investments.tracker.validation.ValidationMessages.WITHDRAWAL_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WithdrawalServiceTest {

    @InjectMocks
    private WithdrawalService withdrawalService;

    @Mock
    private WithdrawalBalanceBuilderService withdrawalBalanceBuilderService;

    @Mock
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private CashTransactionMapper cashTransactionMapper;

    @Mock
    private WithdrawalMapper withdrawalMapper;

    private WithdrawalRequest withdrawalRequest;

    private final LocalDate DATE = LocalDate.of(2025, 1, 1);

    @BeforeEach
    void setUp() {
        withdrawalRequest = WithdrawalRequest.builder()
                .date(DATE)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .build();
    }

    @Test
    @DisplayName("Test should return error when no balance exists")
    void testShouldReturnErrorWhenNoBalanceExists() {
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> withdrawalService.insertWithdraw(withdrawalRequest));
        assertEquals(WITHDRAWAL_NOT_POSSIBLE_BALANCE_DOES_NOT_EXIST, exception.getReason());
    }

    @Test
    @DisplayName("Test should return error when withdrawal date is before latest balance date")
    void testShouldReturnErrorWhenWithdrawalDateIsBeforeLatestBalanceDate() {
        Balance mockBalanceWithDateAfterWithdrawal = Balance.builder()
                .date(LocalDate.of(2030, 1, 1))
                .balance(BigDecimal.valueOf(1000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(mockBalanceWithDateAfterWithdrawal));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> withdrawalService.insertWithdraw(withdrawalRequest));
        assertEquals(WITHDRAWAL_DATE_NOT_BEFORE_LATEST_BALANCE, exception.getReason());
    }


    @Test
    @DisplayName("Test should return error when there is not enough money")
    void testShouldReturnErrorWhenThereIsNoEnoughMoney() {
        Balance mockBalanceWithNotEnoughMoney = Balance.builder()
                .date(LocalDate.of(2024, 1, 1))
                .balance(BigDecimal.valueOf(100))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();
        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(mockBalanceWithNotEnoughMoney));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> withdrawalService.insertWithdraw(withdrawalRequest));
        assertTrue(ex.getReason().contains("You don't have enough money to withdraw. Current balance is "));
    }

    @Test
    @DisplayName("Test should create successful withdrawal")
    void testShouldCreateSuccessfulWithdrawal() {
        CashTransaction withdrawal = CashTransaction.builder()
                .date(DATE)
                .cashTransactionType(WITHDRAWAL)
                .amount(BigDecimal.valueOf(1000))
                .currency(EUR)
                .description("")
                .build();

        Balance mockBalanceWithEnoughMoney = Balance.builder()
                .date(DATE)
                .balance(BigDecimal.valueOf(5000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.ZERO)
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();

        Balance mockBalanceWithEnoughMoneyAfterWithdrawal = Balance.builder()
                .date(DATE)
                .balance(BigDecimal.valueOf(4000))
                .totalInvestments(BigDecimal.ZERO)
                .totalDeposits(BigDecimal.ZERO)
                .totalWithdrawals(BigDecimal.valueOf(1000))
                .totalDividends(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .lastPortfolioValue(BigDecimal.ZERO)
                .build();

        when(balanceRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(mockBalanceWithEnoughMoney));
        when(cashTransactionMapper.createCashtransaction(withdrawalRequest, withdrawalMapper)).thenReturn(withdrawal);
        when(cashTransactionRepository.save(any(CashTransaction.class))).thenReturn(withdrawal);
        when(withdrawalBalanceBuilderService.createBalanceFromCashTransaction(eq(mockBalanceWithEnoughMoney), eq(withdrawal))).thenReturn(mockBalanceWithEnoughMoneyAfterWithdrawal);

        BalanceResponse balanceResponse = withdrawalService.insertWithdraw(withdrawalRequest);
        assertEquals(0, balanceResponse.balance().compareTo(BigDecimal.valueOf(4000)));
        assertEquals(0, balanceResponse.totalWithdrawals().compareTo(BigDecimal.valueOf(1000)));

        verify(cashTransactionMapper).createCashtransaction(eq(withdrawalRequest), eq(withdrawalMapper));
        verify(cashTransactionRepository).save(any(CashTransaction.class));
        verify(balanceRepository).findTopByOrderByIdDesc();
        verify(withdrawalBalanceBuilderService).createBalanceFromCashTransaction(eq(mockBalanceWithEnoughMoney), eq(withdrawal));
        verify(balanceRepository).save(any(Balance.class));
    }


}
