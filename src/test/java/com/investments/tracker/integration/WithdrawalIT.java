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
import static com.investments.tracker.validation.ValidationMessages.*;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class WithdrawalIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${application.test.deposit-insert-url}")
    private String depositInsertUrl;

    @Value("${application.test.withdrawal-create-url}")
    private String withdrawalCreateUrl;

    @Test
    @DisplayName("Test successful creation of a withdrawal")
    void testSuccessfulCreationOfAWithdrawal() throws Exception {
        String depositRequestBodyJson = new String(new ClassPathResource("json/deposit/correct-deposit-request.json").getInputStream().readAllBytes());
        mockMvc.perform(post(depositInsertUrl)
                        .contentType(APPLICATION_JSON)
                        .content(depositRequestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON));

        String correctWithdrawalRequestBodyJson = new String(new ClassPathResource("json/withdrawal/correct-withdrawal-request.json").getInputStream().readAllBytes());
        String expectedResponseBodyAfterCorrectWithdrawal = new String(new ClassPathResource("json/withdrawal/balance-after-correct-withdrawal-response.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(correctWithdrawalRequestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(expectedResponseBodyAfterCorrectWithdrawal));
    }

    @Test
    @DisplayName("Test create withdrawal with: null date")
    void testCreateWithdrawalWithNullDate() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/null-date-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DATE_NOT_NULL_NOR_EMPTY)));
    }

    @Test
    @DisplayName("Test create withdrawal with: empty date")
    void testCreateWithdrawalWithEmptyDate() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/empty-date-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DATE_NOT_NULL_NOR_EMPTY)));
    }

    @Test
    @DisplayName("Test create withdrawal with: future date")
    void testCreateWithdrawalWithFutureDate() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/future-date-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DATE_NOT_IN_FUTURE)));
    }

    @Test
    @DisplayName("Test create withdrawal with: null amount")
    void testCreateWithdrawalWithNullAmount() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/null-amount-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_NOT_NULL)));
    }

    @Test
    @DisplayName("Test create withdrawal with: empty amount")
    void testCreateWithdrawalWithEmptyAmount() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/empty-amount-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_NOT_NULL)));
    }

    @Test
    @DisplayName("Test create withdrawal with: negative amount")
    void testCreateWithdrawalWithNegativeAmount() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/negative-amount-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_MORE_THAN_ZERO)));
    }

    @Test
    @DisplayName("Test create withdrawal with: zero amount")
    void testCreateWithdrawalWithZeroAmount() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/zero-amount-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_MORE_THAN_ZERO)));
    }

    @Test
    @DisplayName("Test create withdrawal with: incorrect digits amount")
    void testCreateWithdrawalWithIncorrectDigitsAmount() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/incorrect-digits-amount-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(AMOUNT_DIGITS)));
    }

    @Test
    @DisplayName("Test create withdrawal with: null currency")
    void testCreateWithdrawalWithNullCurrency() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/null-currency-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(CURRENCY_NOT_NULL)));
    }

    @Test
    @DisplayName("Test create withdrawal with: empty currency")
    void testCreateWithdrawalWithEmptyCurrency() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/empty-currency-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred. Please contact support."));
    }

    @Test
    @DisplayName("Test create withdrawal with: incorrect currency")
    void testCreateWithdrawalWithIncorrectCurrency() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/incorrect-currency-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred. Please contact support."));
    }

    @Test
    @DisplayName("Test create withdrawal with: null description")
    void testCreateWithdrawalWithNullDescription() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/null-description-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DESCRIPTION_NOT_NULL)));
    }

    @Test
    @DisplayName("Test create withdrawal with: empty description")
    void testCreateWithdrawalWithEmptyDescription() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/empty-description-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DESCRIPTION_NOT_EMPTY)));
    }

    @Test
    @DisplayName("Test create withdrawal with: too long description")
    void testCreateWithdrawalWithDescriptionLongerThan255() throws Exception {
        String withdrawalRequest = new String(new ClassPathResource("json/withdrawal/too-long-description-withdrawal-request.json").getInputStream().readAllBytes());

        mockMvc.perform(post(withdrawalCreateUrl)
                        .contentType(APPLICATION_JSON)
                        .content(withdrawalRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(DESCRIPTION_NOT_EMPTY)));
    }

}
