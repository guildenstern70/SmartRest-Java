/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateDealerDto(
    @NotBlank(message = "Dealer name is required")
    @Size(max = 100, message = "Dealer name must not exceed 100 characters")
    String name,

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    String city,

    @NotNull(message = "Brand IDs are required")
    @Size(min = 1, max = 2, message = "A car dealer must have exactly one or two brands")
    Set<Long> brandIds
)
{
}
