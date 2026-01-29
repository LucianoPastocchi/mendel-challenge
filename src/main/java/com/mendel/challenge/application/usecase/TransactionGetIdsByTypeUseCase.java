package com.mendel.challenge.application.usecase;

import java.util.List;

@FunctionalInterface
public interface TransactionGetIdsByTypeUseCase {

    List<Long> getIdsByType(String type);
}
