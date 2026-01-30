package com.mendel.challenge.infrastructure.controller;

import com.mendel.challenge.application.usecase.TransactionGetIdsByTypeUseCase;
import com.mendel.challenge.application.usecase.TransactionGetSumByParentIdUseCase;
import com.mendel.challenge.application.usecase.TransactionSaveUseCase;
import com.mendel.challenge.domain.model.Transaction;
import com.mendel.challenge.domain.model.TransactionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController("/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionSaveUseCase transactionUseCase;
    private final TransactionGetIdsByTypeUseCase transactionGetIdsByTypeUseCase;
    private final TransactionGetSumByParentIdUseCase transactionGetSumUseCase;

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

        log.info("Saved transaction with id {}", id);

        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<List<Long>> getIdsByType(
            @PathVariable String type) {
        return ResponseEntity.ok(transactionGetIdsByTypeUseCase.getIdsByType(type));
    }

    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<Map<String, Double>> getTransitiveSum(
            @PathVariable("transaction_id") Long transactionId) {
        return ResponseEntity.ok(transactionGetSumUseCase.getTransitiveSum(transactionId));
    }

}
