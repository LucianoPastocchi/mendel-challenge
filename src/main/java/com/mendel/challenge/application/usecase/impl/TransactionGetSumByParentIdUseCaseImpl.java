package com.mendel.challenge.application.usecase.impl;

import com.mendel.challenge.application.ports.TransactionPort;
import com.mendel.challenge.application.usecase.TransactionGetSumByParentIdUseCase;
import com.mendel.challenge.domain.exception.TransactionException;
import com.mendel.challenge.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TransactionGetSumByParentIdUseCaseImpl implements TransactionGetSumByParentIdUseCase {

    private final TransactionPort transactionPort;

    @Override
    public Map<String, Double> getTransitiveSum(Long transactionId) {
        Transaction transaction = transactionPort.getTransactionById(transactionId)
                .orElseThrow(() -> new TransactionException("Transaction not found"));

        return Map.of("sum", calculateSum(transaction));
    }

    private double calculateSum(Transaction transaction) {
        double sum = transaction.amount();

        List<Transaction> children = transactionPort.getTransactionsByParentId(transaction.id());

        for (Transaction child : children) {
            sum += calculateSum(child);
        }

        return sum;
    }
}
