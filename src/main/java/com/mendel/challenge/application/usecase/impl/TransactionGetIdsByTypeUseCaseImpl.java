package com.mendel.challenge.application.usecase.impl;

import com.mendel.challenge.application.ports.TransactionPort;
import com.mendel.challenge.application.usecase.TransactionGetIdsByTypeUseCase;
import com.mendel.challenge.domain.exception.TransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionGetIdsByTypeUseCaseImpl implements TransactionGetIdsByTypeUseCase {

    private final TransactionPort transactionPort;

    @Override
    public List<Long> getIdsByType(String type) {

        if (type == null || type.trim().isEmpty()) {
            throw new TransactionException("Type cannot be null or empty");
        }

        String sanitizedType = type.trim();

        if (!sanitizedType.matches("^[a-zA-Z0-9_-]+$")) {
            throw new TransactionException("Invalid type format: only alphanumeric characters, underscores, and hyphens are allowed");
        }

        return transactionPort.getIdsByType(sanitizedType);
    }
}
