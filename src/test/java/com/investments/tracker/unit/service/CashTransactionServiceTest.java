package com.investments.tracker.unit.service;

import com.investments.tracker.controller.cashtransaction.CashTransactionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.investments.tracker.enums.CashTransactionType.DEPOSIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CashTransactionServiceTest {
//    @Test
//    @DisplayName("Test should return all deposits from [date] to [date] when we have deposits")
//    void testGetAllDepositsFromToNotEmpty() {
//        when(cashTransactionRepository.findByCashTransactionTypeAndDateBetween(eq(DEPOSIT), eq(DATE), eq(DATE))).thenReturn(List.of(deposit));
//        when(cashTransactionMapper.mapToResponseDTOList(eq(List.of(deposit)), eq(DEPOSIT))).thenReturn(List.of(cashTransactionResponse));
//
//        List<CashTransactionResponse> result = depositService.getAllDepositsFromTo(DATE, DATE);
//        assertEquals(1, result.size());
//        assertEquals(result.get(0).amount(), BigDecimal.valueOf(1000));
//
//        verify(cashTransactionRepository).findByCashTransactionTypeAndDateBetween(DEPOSIT, DATE, DATE);
//        verify(cashTransactionMapper).mapToResponseDTOList(List.of(deposit), DEPOSIT);
//    }
//
//    @Test
//    @DisplayName("Test should return all deposits from [date] to [date] when we don't have deposits")
//    void testGetAllDepositsFromToEmpty() {
//        when(cashTransactionRepository.findByCashTransactionTypeAndDateBetween(eq(DEPOSIT), eq(DATE), eq(DATE))).thenReturn(Collections.emptyList());
//
//        List<CashTransactionResponse> result = depositService.getAllDepositsFromTo(DATE, DATE);
//        assertEquals(0, result.size());
//
//        verify(cashTransactionRepository).findByCashTransactionTypeAndDateBetween(DEPOSIT, DATE, DATE);
//        verifyNoInteractions(depositMapper);
//    }
//
//    @Test
//    @DisplayName("Test should return total amount of all deposits when we have deposits")
//    void testGetTotalDepositsAmountNotEmpty() {
//        when(cashTransactionRepository.getTotalAmountOf(DEPOSIT)).thenReturn(Optional.of(balance.getTotalDeposits()));
//
//        BigDecimal result = depositService.getTotalDepositsAmount();
//        assertEquals(0, result.compareTo(BigDecimal.valueOf(1000)));
//
//        verify(cashTransactionRepository).getTotalAmountOf(DEPOSIT);
//    }
//
//    @Test
//    @DisplayName("Test should return total amount of all deposits when we don't have deposits")
//    void testGetTotalDepositsAmountEmpty() {
//        when(cashTransactionRepository.getTotalAmountOf(DEPOSIT)).thenReturn(Optional.empty());
//
//        BigDecimal result = depositService.getTotalDepositsAmount();
//        assertEquals(0, result.compareTo(BigDecimal.ZERO));
//
//        verify(cashTransactionRepository).getTotalAmountOf(DEPOSIT);
//    }
}
