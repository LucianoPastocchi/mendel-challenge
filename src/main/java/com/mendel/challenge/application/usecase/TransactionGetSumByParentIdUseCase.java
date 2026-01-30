package com.mendel.challenge.application.usecase;

import com.mendel.challenge.domain.model.Transaction;
import java.util.Map;

@FunctionalInterface
public interface TransactionGetSumByParentIdUseCase {
    Map<String, Double> getTransitiveSum(Long parentId);
}
