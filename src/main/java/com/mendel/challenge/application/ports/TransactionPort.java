package com.mendel.challenge.application.ports;

import com.mendel.challenge.domain.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface TransactionPort {

    void saveTransaction(Transaction transaction);
    List<Transaction> getTransactionsByParentId(Long parentId);
    List<Long> getIdsByType(String type);
   Optional<Transaction> getTransactionById(Long id);
}
