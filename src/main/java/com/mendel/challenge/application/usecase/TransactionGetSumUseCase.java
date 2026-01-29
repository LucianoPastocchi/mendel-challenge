package com.mendel.challenge.application.usecase;

import com.mendel.challenge.domain.model.Transaction;
import java.util.Map;

@FunctionalInterface
public interface TransactionGetSumUseCase {
    Map<String, Double> getTransactionSum(Transaction transaction);
}
