package com.mendel.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record TransactionRequest(
     @NotNull
     @Positive
     Double amount,

     @NotBlank
     @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Type must contain only alphanumeric characters, underscores, or hyphens")
     @Size(min = 1, max = 50, message = "Type must be between 1 and 50 characters")
     String type,

     @JsonProperty("parent_id")
     @Positive
     Long parentId) {

}
