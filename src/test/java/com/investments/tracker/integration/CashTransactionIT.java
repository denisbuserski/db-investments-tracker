package com.investments.tracker.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class CashTransactionIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${application.test.deposit-insert-url}")
    private String depositInsertUrl;

    @Value("${application.test.cashtransaction-get-from-to-url}")
    private String cashTransactionGetFromToWithDatesUrl;

    @Value("${application.test.cashtransaction-get-from-to-no-date-url}")
    private String cashTransactionGetFromToWithNoDatesUrl;

    @Value("${application.test.cashtransaction-get-total-amount-url}")
    private String cashTransactionGetTotalAmountUrl;


    @Test
    @DisplayName("Test get cash transactions from to with dates")
    void testGetCashTransactionsFromToWithDates() throws Exception {
        String correctDepositRequestBodyJson = new String(new ClassPathResource("json/deposit/correct-deposit-request.json").getInputStream().readAllBytes());
        String cashTransactionResponseJson = new String(new ClassPathResource("json/cashtransaction/cashtransaction-response.json").getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(correctDepositRequestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));

        mockMvc.perform(get(cashTransactionGetFromToWithDatesUrl)
                        .param("cashTransactionType", "DEPOSIT")
                        .param("fromDate", "2025-01-01")
                        .param("toDate", "2026-01-01")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(cashTransactionResponseJson));
    }

    @Test
    @DisplayName("Test get cash transactions from to with no dates")
    void testGetCashTransactionsFromToWithNoDates() throws Exception {
        String correctDepositRequestBodyJson = new String(new ClassPathResource("json/deposit/correct-deposit-request.json").getInputStream().readAllBytes());
        String cashTransactionResponseJson = new String(new ClassPathResource("json/cashtransaction/cashtransaction-response.json").getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(correctDepositRequestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));

        mockMvc.perform(get(cashTransactionGetFromToWithNoDatesUrl)
                        .param("cashTransactionType", "DEPOSIT")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(cashTransactionResponseJson));
    }

    @Test
    @DisplayName("Test get total cash transactions amount")
    void testGetTotalCashTransactionsAmount() throws Exception {
        String correctDepositRequestBodyJson = new String(new ClassPathResource("json/deposit/correct-deposit-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(correctDepositRequestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));

        mockMvc.perform(get(cashTransactionGetTotalAmountUrl)
                        .param("cashTransactionType", "DEPOSIT")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string("5000.00"));
    }

}
