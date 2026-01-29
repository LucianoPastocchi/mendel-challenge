package com.mendel.challenge.infrastructure.controller;

import com.mendel.challenge.application.usecase.TransactionSaveUseCase;
import com.mendel.challenge.domain.model.Transaction;
import com.mendel.challenge.domain.model.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionSaveUseCase transactionUseCase;

    @PutMapping("/{transaction_id}")
    public ResponseEntity<Map<String, String>> save(
            @PathVariable("transaction_id") Long id,
            @RequestBody TransactionRequest request) {

        Transaction transaction = new Transaction(
                id,
                request.amount(),
                request.type(),
                request.parentId()
        );

        transactionUseCase.saveTransaction(transaction);

        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
