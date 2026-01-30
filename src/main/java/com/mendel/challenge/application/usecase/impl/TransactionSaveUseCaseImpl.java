package com.mendel.challenge.application.usecase.impl;

import com.mendel.challenge.application.ports.TransactionPort;
import com.mendel.challenge.application.usecase.TransactionSaveUseCase;
import com.mendel.challenge.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionSaveUseCaseImpl implements TransactionSaveUseCase {

    private final TransactionPort transactionPort;

    @Override
    public void saveTransaction(Transaction transaction) {
        if (transaction.id() != null && transaction.id().equals(transaction.parentId())) {
            throw new IllegalArgumentException("Parent ID cannot be the same as transaction ID");
        }
        if (transaction.parentId() != null && transactionPort.getTransactionById(transaction.parentId()).isEmpty()) {
            throw new IllegalArgumentException("Parent transaction does not exist");
        }
        transactionPort.saveTransaction(transaction);
    }

}
