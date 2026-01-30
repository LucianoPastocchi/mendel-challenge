package com.mendel.challenge.application.usecase;

import com.mendel.challenge.domain.model.Transaction;

@FunctionalInterface
public interface TransactionSaveUseCase {
    void saveTransaction(Transaction transaction);
}
