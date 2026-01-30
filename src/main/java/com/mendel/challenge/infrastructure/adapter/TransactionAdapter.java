package com.mendel.challenge.infrastructure.adapter;

import com.mendel.challenge.application.ports.TransactionPort;
import com.mendel.challenge.domain.model.Transaction;
import com.mendel.challenge.infrastructure.entity.TransactionEntity;
import com.mendel.challenge.infrastructure.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionAdapter implements TransactionPort {

    private final TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(TransactionEntity.fromDomain(transaction));
    }

    @Override
    public List<Transaction> getTransactionsByParentId(Long parentId) {
        return transactionRepository.findByParentId(parentId).stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }

    @Override
    public List<Long> getIdsByType(String type) {
        return transactionRepository.findByType(type);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(TransactionEntity::toDomain);
    }
}
