package com.investments.tracker.integration;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.investments.tracker.validation.ValidationMessages.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class DepositIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${application.test.deposit-insert-url}")
    private String depositInsertUrl;

    @Value("classpath:json/deposit/correct-deposit-request.json")
    private Resource correctDepositRequestJson;

    @Value("classpath:json/deposit/balance-after-correct-deposit-response.json")
    private Resource balanceAfterCorrectDepositJson;

    @Value("classpath:json/deposit/null-date-deposit-request.json")
    private Resource nullDateDepositRequesstJson;

    @Value("classpath:json/deposit/empty-date-deposit-request.json")
    private Resource emptyDateDepositRequestJson;

    @Value("classpath:json/deposit/future-date-deposit-request.json")
    private Resource futureDateDepositRequestJson;

    @Value("classpath:json/deposit/null-amount-deposit-request.json")
    private Resource nullAmountDepositRequestJson;

    @Value("classpath:json/deposit/empty-amount-deposit-request.json")
    private Resource emptyAmountDepositRequestJson;

    @Value("classpath:json/deposit/negative-amount-deposit-request.json")
    private Resource negativeAmountDepositRequestJson;

    @Value("classpath:json/deposit/zero-amount-deposit-request.json")
    private Resource zeroAmountDepositRequestJson;

    @Value("classpath:json/deposit/incorrect-digits-amount-deposit-request.json")
    private Resource incorrectDigitsAmountDepositRequestJson;

    @Value("classpath:json/deposit/null-currency-deposit-request.json")
    private Resource nullCurrencyDepositRequestJson;

    @Value("classpath:json/deposit/empty-currency-deposit-request.json")
    private Resource emptyCurrencyDepositRequestJson;

    @Value("classpath:json/deposit/incorrect-currency-deposit-request.json")
    private Resource incorrectCurrencyDepositRequestJson;

    @Value("classpath:json/deposit/null-description-deposit-request.json")
    private Resource nullDescriptionDepositRequestJson;

    @Value("classpath:json/deposit/empty-description-deposit-request.json")
    private Resource emptyDescriptionDepositRequestJson;

    @Value("classpath:json/deposit/too-long-description-deposit-request.json")
    private Resource tooLongDescriptionDepositRequestJson;


    @Test
    @DisplayName("Test should successfully insert a deposit")
    void testSuccessfulInsertionOfADeposit() throws Exception {
        String correctDepositRequestBodyJson = new String(correctDepositRequestJson.getInputStream().readAllBytes());
        String expectedResponseBodyAfterCorrectDeposit = new String(balanceAfterCorrectDepositJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(correctDepositRequestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(expectedResponseBodyAfterCorrectDeposit));
    }

    @Test
    @DisplayName("Test insert deposit with: null date")
    void testInsertDepositWithNullDate() throws Exception {
        String depositRequest = new String(nullDateDepositRequesstJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DATE_NOT_NULL_NOR_EMPTY)));
    }

    @Test
    @DisplayName("Test insert deposit with: empty date")
    void testInsertDepositWithEmptyDate() throws Exception {
        String depositRequest = new String(emptyDateDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DATE_NOT_NULL_NOR_EMPTY)));
    }

    @Test
    @DisplayName("Test insert deposit with: future date")
    void testInsertDepositWithFutureDate() throws Exception {
        String depositRequest = new String(futureDateDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DATE_NOT_IN_FUTURE)));
    }

    @Test
    @DisplayName("Test insert deposit with: null amount")
    void testInsertDepositWithNullAmount() throws Exception {
        String depositRequest = new String(nullAmountDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_NOT_NULL)));
    }

    @Test
    @DisplayName("Test insert deposit with: empty amount")
    void testInsertDepositWithEmptyAmount() throws Exception {
        String depositRequest = new String(emptyAmountDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_NOT_NULL)));
    }

    @Test
    @DisplayName("Test insert deposit with: negative amount")
    void testInsertDepositWithNegativeAmount() throws Exception {
        String depositRequest = new String(negativeAmountDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_MORE_THAN_ZERO)));
    }

    @Test
    @DisplayName("Test insert deposit with: zero amount")
    void testInsertDepositWithZeroAmount() throws Exception {
        String depositRequest = new String(zeroAmountDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_MORE_THAN_ZERO)));
    }

    @Test
    @DisplayName("Test insert deposit with: incorrect digits amount")
    void testInsertDepositWithIncorrectDigitsAmount() throws Exception {
        String depositRequest = new String(incorrectDigitsAmountDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_DIGITS)));
    }

    @Test
    @DisplayName("Test insert deposit with: null currency")
    void testInsertDepositWithNullCurrency() throws Exception {
        String depositRequest = new String(nullCurrencyDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(CURRENCY_NOT_NULL)));
    }

    @Test
    @DisplayName("Test insert deposit with: empty currency")
    void testInsertDepositWithEmptyCurrency() throws Exception {
        String depositRequest = new String(emptyCurrencyDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred. Please contact support."));
    }

    @Test
    @DisplayName("Test insert deposit with: incorrect currency")
    void testInsertDepositWithIncorrectCurrency() throws Exception {
        String depositRequest = new String(incorrectCurrencyDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred. Please contact support."));
    }

    @Test
    @DisplayName("Test insert deposit with: null description")
    void testInsertDepositWithNullDescription() throws Exception {
        String depositRequest = new String(nullDescriptionDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DESCRIPTION_NOT_NULL)));
    }

    @Test
    @DisplayName("Test insert deposit with: empty description")
    void testInsertDepositWithEmptyDescription() throws Exception {
        String depositRequest = new String(emptyDescriptionDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DESCRIPTION_NOT_EMPTY)));
    }

    @Test
    @DisplayName("Test insert deposit with: too long description")
    void testInsertDepositWithTooLongDescription() throws Exception {
        String depositRequest = new String(tooLongDescriptionDepositRequestJson.getInputStream().readAllBytes());

        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DESCRIPTION_NOT_EMPTY)));
    }














}
