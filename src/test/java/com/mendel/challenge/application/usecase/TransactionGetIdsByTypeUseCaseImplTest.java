package com.mendel.challenge.application.usecase;

import com.mendel.challenge.application.ports.TransactionPort;
import com.mendel.challenge.application.usecase.impl.TransactionGetIdsByTypeUseCaseImpl;
import com.mendel.challenge.domain.exception.TransactionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionGetIdsByTypeUseCaseImplTest {

    @Mock
    private TransactionPort transactionPort;

    @InjectMocks
    private TransactionGetIdsByTypeUseCaseImpl transactionGetIdsByTypeUseCase;

    @Test
    void shouldReturnIdsByType() {
        String type = "shopping";
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);
        when(transactionPort.getIdsByType(type)).thenReturn(expectedIds);

        List<Long> result = transactionGetIdsByTypeUseCase.getIdsByType(type);

        assertEquals(expectedIds, result);
        verify(transactionPort, times(1)).getIdsByType(type);
    }

    @Test
    void shouldTrimTypeBeforeSearch() {
        String type = "  shopping  ";
        List<Long> expectedIds = Arrays.asList(1L, 2L);
        when(transactionPort.getIdsByType("shopping")).thenReturn(expectedIds);

        List<Long> result = transactionGetIdsByTypeUseCase.getIdsByType(type);

        assertEquals(expectedIds, result);
        verify(transactionPort, times(1)).getIdsByType("shopping");
    }

    @Test
    void shouldThrowExceptionWhenTypeIsNull() {
        assertThrows(TransactionException.class, () -> {
            transactionGetIdsByTypeUseCase.getIdsByType(null);
        });

        verify(transactionPort, never()).getIdsByType(any());
    }

    @Test
    void shouldThrowExceptionWhenTypeIsEmpty() {
        assertThrows(TransactionException.class, () -> {
            transactionGetIdsByTypeUseCase.getIdsByType("");
        });

        verify(transactionPort, never()).getIdsByType(any());
    }

    @Test
    void shouldThrowExceptionWhenTypeIsBlank() {
        assertThrows(TransactionException.class, () -> {
            transactionGetIdsByTypeUseCase.getIdsByType("   ");
        });

        verify(transactionPort, never()).getIdsByType(any());
    }

    @Test
    void shouldReturnEmptyListWhenNoTransactionsFound() {
        String type = "nonexistent";
        when(transactionPort.getIdsByType(type)).thenReturn(List.of());

        List<Long> result = transactionGetIdsByTypeUseCase.getIdsByType(type);

        assertTrue(result.isEmpty());
        verify(transactionPort, times(1)).getIdsByType(type);
    }
}
