package com.mendel.challenge.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendel.challenge.application.usecase.TransactionGetIdsByTypeUseCase;
import com.mendel.challenge.application.usecase.TransactionGetSumByParentIdUseCase;
import com.mendel.challenge.application.usecase.TransactionSaveUseCase;
import com.mendel.challenge.domain.exception.TransactionException;
import com.mendel.challenge.domain.model.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionSaveUseCase transactionSaveUseCase;

    @MockitoBean
    private TransactionGetIdsByTypeUseCase transactionGetIdsByTypeUseCase;

    @MockitoBean
    private TransactionGetSumByParentIdUseCase transactionGetSumUseCase;

    @Test
    void shouldSaveTransaction() throws Exception {
        TransactionRequest request = new TransactionRequest(1000.0, "shopping", null);

        mockMvc.perform(put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));

        verify(transactionSaveUseCase, times(1)).saveTransaction(any());
    }

    @Test
    void shouldSaveTransactionWithParentId() throws Exception {
        TransactionRequest request = new TransactionRequest(500.0, "shopping", 1L);

        mockMvc.perform(put("/transactions/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));

        verify(transactionSaveUseCase, times(1)).saveTransaction(any());
    }

    @Test
    void shouldGetIdsByType() throws Exception {
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);
        when(transactionGetIdsByTypeUseCase.getIdsByType("shopping")).thenReturn(expectedIds);

        mockMvc.perform(get("/transactions/types/shopping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(2))
                .andExpect(jsonPath("$[2]").value(3));

        verify(transactionGetIdsByTypeUseCase, times(1)).getIdsByType("shopping");
    }

    @Test
    void shouldReturnEmptyListWhenNoTransactionsOfType() throws Exception {
        when(transactionGetIdsByTypeUseCase.getIdsByType("nonexistent")).thenReturn(List.of());

        mockMvc.perform(get("/transactions/types/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    void shouldGetTransitiveSum() throws Exception {
        when(transactionGetSumUseCase.getTransitiveSum(10L))
                .thenReturn(Map.of("sum", 20000.0));

        mockMvc.perform(get("/transactions/sum/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(20000.0));

        verify(transactionGetSumUseCase, times(1)).getTransitiveSum(10L);
    }

    @Test
    void shouldReturnBadRequestWhenTransactionNotFoundForSum() throws Exception {
        when(transactionGetSumUseCase.getTransitiveSum(999L))
                .thenThrow(new TransactionException("Transaction not found"));

        mockMvc.perform(get("/transactions/sum/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Transaction not found"));

        verify(transactionGetSumUseCase, times(1)).getTransitiveSum(999L);
    }

    @Test
    void shouldGetSumForTransactionWithNoChildren() throws Exception {
        when(transactionGetSumUseCase.getTransitiveSum(1L))
                .thenReturn(Map.of("sum", 1000.0));

        mockMvc.perform(get("/transactions/sum/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(1000.0));
    }
}
