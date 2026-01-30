package com.mendel.challenge.infrastructure.controller;

import com.mendel.challenge.application.usecase.TransactionGetIdsByTypeUseCase;
import com.mendel.challenge.application.usecase.TransactionGetSumByParentIdUseCase;
import com.mendel.challenge.application.usecase.TransactionSaveUseCase;
import com.mendel.challenge.domain.model.Transaction;
import com.mendel.challenge.domain.model.TransactionRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TransactionController {

    private final TransactionSaveUseCase transactionUseCase;
    private final TransactionGetIdsByTypeUseCase transactionGetIdsByTypeUseCase;
    private final TransactionGetSumByParentIdUseCase transactionGetSumUseCase;

    @PutMapping("/{transaction_id}")
    public ResponseEntity<Map<String, String>> save(
            @PathVariable("transaction_id") Long id,
            @Valid @RequestBody TransactionRequest request) {

        Transaction transaction = Transaction.builder()
                .id(id)
                .amount(request.amount())
                .type(request.type())
                .parentId(request.parentId())
                .build();

        transactionUseCase.saveTransaction(transaction);

        log.info("Saved transaction with id {}", id);

        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<List<Long>> getIdsByType(
            @PathVariable
            @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Type must contain only alphanumeric characters, underscores, or hyphens")
            String type) {
        return ResponseEntity.ok(transactionGetIdsByTypeUseCase.getIdsByType(type));
    }

    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<Map<String, Double>> getTransitiveSum(
            @PathVariable("transaction_id") Long transactionId) {
        return ResponseEntity.ok(transactionGetSumUseCase.getTransitiveSum(transactionId));
    }

}
