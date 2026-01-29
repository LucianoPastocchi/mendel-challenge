package com.mendel.challenge.application.ports;

import com.mendel.challenge.domain.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface TransactionPort {

    Long saveTransaction(Transaction transaction);
    Map<String, Double> getTransactionSum(Transaction transaction);
    List<Long> getIdsByType(String type);

}
