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
        transactionPort.saveTransaction(transaction);
    }
}
