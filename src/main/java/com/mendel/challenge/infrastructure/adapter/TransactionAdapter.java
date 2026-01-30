package com.mendel.challenge.infrastructure.adapter;

import com.mendel.challenge.application.ports.TransactionPort;
import com.mendel.challenge.domain.model.Transaction;
import com.mendel.challenge.infrastructure.entity.TransactionEntity;
import com.mendel.challenge.infrastructure.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TransactionAdapter implements TransactionPort {

    private final TransactionRepository transactionRepository;

    @Override
    public Long saveTransaction(Transaction transaction) {
        TransactionEntity transactionEntity = TransactionEntity.fromDomain(transaction);
        transactionRepository.save(TransactionEntity.fromDomain(transaction));
        return transactionEntity.getId();
    }

    @Override
    public Map<String, Double> getTransactionSum(Transaction transaction) {
        return Map.of();
    }

    @Override
    public List<Long> getIdsByType(String type) {
        return transactionRepository.findByType(type);
    }
}
