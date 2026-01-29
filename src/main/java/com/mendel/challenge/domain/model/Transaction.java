package com.mendel.challenge.domain.model;

import lombok.Builder;

@Builder
public record Transaction(
        Long id,
        Double amount,
        String type,
        Long parentId
) {
}
