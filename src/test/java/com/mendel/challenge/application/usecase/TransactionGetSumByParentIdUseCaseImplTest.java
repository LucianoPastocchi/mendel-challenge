package com.mendel.challenge.application.usecase;

import com.mendel.challenge.application.ports.TransactionPort;
import com.mendel.challenge.application.usecase.impl.TransactionGetSumByParentIdUseCaseImpl;
import com.mendel.challenge.domain.exception.TransactionException;
import com.mendel.challenge.domain.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionGetSumByParentIdUseCaseImplTest {

    @Mock
    private TransactionPort transactionPort;

    @InjectMocks
    private TransactionGetSumByParentIdUseCaseImpl transactionGetSumUseCase;

    @Test
    void shouldReturnSumOfSingleTransaction() {
        Transaction transaction = Transaction.builder()
                .id(1L)
                .amount(1000.0)
                .type("shopping")
                .build();

        when(transactionPort.getTransactionById(1L)).thenReturn(Optional.of(transaction));
        when(transactionPort.getTransactionsByParentId(1L)).thenReturn(List.of());

        Map<String, Double> result = transactionGetSumUseCase.getTransitiveSum(1L);

        assertEquals(1000.0, result.get("sum"));
        verify(transactionPort, times(1)).getTransactionById(1L);
        verify(transactionPort, times(1)).getTransactionsByParentId(1L);
    }

    @Test
    void shouldReturnTransitiveSumWithChildren() {
        Transaction parent = Transaction.builder()
                .id(10L)
                .amount(5000.0)
                .type("cars")
                .build();

        Transaction child1 = Transaction.builder()
                .id(11L)
                .amount(10000.0)
                .type("shopping")
                .parentId(10L)
                .build();

        Transaction child2 = Transaction.builder()
                .id(12L)
                .amount(5000.0)
                .type("shopping")
                .parentId(11L)
                .build();

        when(transactionPort.getTransactionById(10L)).thenReturn(Optional.of(parent));
        when(transactionPort.getTransactionsByParentId(10L)).thenReturn(List.of(child1));
        when(transactionPort.getTransactionsByParentId(11L)).thenReturn(List.of(child2));
        when(transactionPort.getTransactionsByParentId(12L)).thenReturn(List.of());

        Map<String, Double> result = transactionGetSumUseCase.getTransitiveSum(10L);

        assertEquals(20000.0, result.get("sum"));
        verify(transactionPort, times(1)).getTransactionById(10L);
        verify(transactionPort, times(1)).getTransactionsByParentId(10L);
        verify(transactionPort, times(1)).getTransactionsByParentId(11L);
        verify(transactionPort, times(1)).getTransactionsByParentId(12L);
    }

    @Test
    void shouldReturnSumWithMultipleDirectChildren() {
        Transaction parent = Transaction.builder()
                .id(1L)
                .amount(1000.0)
                .type("shopping")
                .build();

        Transaction child1 = Transaction.builder()
                .id(2L)
                .amount(500.0)
                .type("shopping")
                .parentId(1L)
                .build();

        Transaction child2 = Transaction.builder()
                .id(3L)
                .amount(300.0)
                .type("shopping")
                .parentId(1L)
                .build();

        when(transactionPort.getTransactionById(1L)).thenReturn(Optional.of(parent));
        when(transactionPort.getTransactionsByParentId(1L)).thenReturn(Arrays.asList(child1, child2));
        when(transactionPort.getTransactionsByParentId(2L)).thenReturn(List.of());
        when(transactionPort.getTransactionsByParentId(3L)).thenReturn(List.of());

        Map<String, Double> result = transactionGetSumUseCase.getTransitiveSum(1L);

        assertEquals(1800.0, result.get("sum"));
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        when(transactionPort.getTransactionById(999L)).thenReturn(Optional.empty());

        assertThrows(TransactionException.class, () -> {
            transactionGetSumUseCase.getTransitiveSum(999L);
        });

        verify(transactionPort, times(1)).getTransactionById(999L);
        verify(transactionPort, never()).getTransactionsByParentId(any());
    }

    @Test
    void shouldHandleComplexTransitiveHierarchy() {
        Transaction t1 = Transaction.builder().id(1L).amount(100.0).type("a").build();
        Transaction t2 = Transaction.builder().id(2L).amount(200.0).type("b").parentId(1L).build();
        Transaction t3 = Transaction.builder().id(3L).amount(300.0).type("c").parentId(1L).build();
        Transaction t4 = Transaction.builder().id(4L).amount(400.0).type("d").parentId(2L).build();
        Transaction t5 = Transaction.builder().id(5L).amount(500.0).type("e").parentId(3L).build();

        when(transactionPort.getTransactionById(1L)).thenReturn(Optional.of(t1));
        when(transactionPort.getTransactionsByParentId(1L)).thenReturn(Arrays.asList(t2, t3));
        when(transactionPort.getTransactionsByParentId(2L)).thenReturn(List.of(t4));
        when(transactionPort.getTransactionsByParentId(3L)).thenReturn(List.of(t5));
        when(transactionPort.getTransactionsByParentId(4L)).thenReturn(List.of());
        when(transactionPort.getTransactionsByParentId(5L)).thenReturn(List.of());

        Map<String, Double> result = transactionGetSumUseCase.getTransitiveSum(1L);

        assertEquals(1500.0, result.get("sum"));
    }
}
