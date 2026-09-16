/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.dto;

import net.littlelite.smartrest.model.Car;
import net.littlelite.smartrest.model.FuelType;
import net.littlelite.smartrest.model.TransmissionType;

import java.math.BigDecimal;

public record CarDto(
    Long id,
    String name,
    Long brandId,
    String brandName,
    Integer power,
    Double zeroToHundredKmh,
    Integer passengers,
    BigDecimal price,
    FuelType type,
    TransmissionType transmission,
    Long dealerId,
    String dealerName
)
{
    public static CarDto fromEntity(Car car)
    {
        if (car == null)
        {
            return null;
        }
        return new CarDto(
            car.getId(),
            car.getName(),
            car.getBrand() != null ? car.getBrand().getId() : null,
            car.getBrand() != null ? car.getBrand().getName() : null,
            car.getPower(),
            car.getZeroToHundredKmh(),
            car.getPassengers(),
            car.getPrice(),
            car.getType(),
            car.getTransmission(),
            car.getDealer() != null ? car.getDealer().getId() : null,
            car.getDealer() != null ? car.getDealer().getName() : null
        );
    }
}
