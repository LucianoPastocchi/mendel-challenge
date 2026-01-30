package com.mendel.challenge.infrastructure.adapter;

import com.mendel.challenge.domain.model.Transaction;
import com.mendel.challenge.infrastructure.entity.TransactionEntity;
import com.mendel.challenge.infrastructure.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionAdapterTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionAdapter transactionAdapter;

    @Test
    void shouldSaveTransaction() {
        Transaction transaction = Transaction.builder()
                .id(1L)
                .amount(1000.0)
                .type("shopping")
                .build();

        TransactionEntity entity = TransactionEntity.fromDomain(transaction);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(entity);

        transactionAdapter.saveTransaction(transaction);

        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    void shouldGetTransactionsByParentId() {
        TransactionEntity entity1 = new TransactionEntity(2L, 500.0, "shopping", 1L);
        TransactionEntity entity2 = new TransactionEntity(3L, 300.0, "shopping", 1L);

        when(transactionRepository.findByParentId(1L)).thenReturn(Arrays.asList(entity1, entity2));

        List<Transaction> result = transactionAdapter.getTransactionsByParentId(1L);

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).id());
        assertEquals(3L, result.get(1).id());
        verify(transactionRepository, times(1)).findByParentId(1L);
    }

    @Test
    void shouldReturnEmptyListWhenNoChildrenFound() {
        when(transactionRepository.findByParentId(999L)).thenReturn(List.of());

        List<Transaction> result = transactionAdapter.getTransactionsByParentId(999L);

        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findByParentId(999L);
    }

    @Test
    void shouldGetIdsByType() {
        List<Long> expectedIds = Arrays.asList(1L, 2L, 3L);
        when(transactionRepository.findByType("shopping")).thenReturn(expectedIds);

        List<Long> result = transactionAdapter.getIdsByType("shopping");

        assertEquals(expectedIds, result);
        verify(transactionRepository, times(1)).findByType("shopping");
    }

    @Test
    void shouldGetTransactionById() {
        TransactionEntity entity = new TransactionEntity(1L, 1000.0, "shopping", null);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Transaction> result = transactionAdapter.getTransactionById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().id());
        assertEquals(1000.0, result.get().amount());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenTransactionNotFound() {
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Transaction> result = transactionAdapter.getTransactionById(999L);

        assertFalse(result.isPresent());
        verify(transactionRepository, times(1)).findById(999L);
    }
}
