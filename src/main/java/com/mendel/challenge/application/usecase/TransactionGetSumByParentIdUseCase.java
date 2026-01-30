package com.mendel.challenge.application.usecase;

import java.util.Map;

@FunctionalInterface
public interface TransactionGetSumByParentIdUseCase {
    Map<String, Double> getTransitiveSum(Long parentId);
}
