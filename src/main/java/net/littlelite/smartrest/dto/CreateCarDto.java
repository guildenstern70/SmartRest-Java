/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import net.littlelite.smartrest.model.FuelType;
import net.littlelite.smartrest.model.TransmissionType;

import java.math.BigDecimal;

public record CreateCarDto(
    @NotBlank(message = "Car name is required")
    @Size(max = 100, message = "Car name must not exceed 100 characters")
    String name,

    @NotNull(message = "Brand ID is required")
    Long brandId,

    @NotNull(message = "Power is required")
    @Positive(message = "Power must be positive")
    Integer power,

    @NotNull(message = "0-100 km/h time is required")
    @Positive(message = "0-100 km/h time must be positive")
    Double zeroToHundredKmh,

    @NotNull(message = "Passengers count is required")
    @Positive(message = "Passengers count must be positive")
    Integer passengers,

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    BigDecimal price,

    @NotNull(message = "Fuel type is required")
    FuelType type,

    @NotNull(message = "Transmission type is required")
    TransmissionType transmission,

    Long dealerId
)
{
}
