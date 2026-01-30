package com.mendel.challenge.application.usecase;

import com.mendel.challenge.application.ports.TransactionPort;
import com.mendel.challenge.application.usecase.impl.TransactionSaveUseCaseImpl;
import com.mendel.challenge.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionSaveUseCaseImplTest {

    @Mock
    private TransactionPort transactionPort;

    @InjectMocks
    private TransactionSaveUseCaseImpl transactionSaveUseCase;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .id(1L)
                .amount(1000.0)
                .type("shopping")
                .parentId(null)
                .build();
    }

    @Test
    void shouldSaveTransaction() {
        transactionSaveUseCase.saveTransaction(transaction);

        verify(transactionPort, times(1)).saveTransaction(transaction);
    }

    @Test
    void shouldSaveTransactionWithParentId() {
        Transaction childTransaction = Transaction.builder()
                .id(2L)
                .amount(500.0)
                .type("shopping")
                .parentId(1L)
                .build();

        transactionSaveUseCase.saveTransaction(childTransaction);

        verify(transactionPort, times(1)).saveTransaction(childTransaction);
    }
}
