/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.littlelite.smartrest.dto.CarDto;
import net.littlelite.smartrest.model.FuelType;
import net.littlelite.smartrest.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Tag(name = "Cars", description = "Endpoints for querying car specifications and listings")
public class CarController
{
    private final CarService carService;

    @GetMapping
    @Operation(summary = "Get all cars, optionally filtered by brand, dealer, or fuel type")
    public ResponseEntity<List<CarDto>> getCars(
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long dealerId,
            @RequestParam(required = false) FuelType fuelType)
    {
        if (brandId != null)
        {
            return ResponseEntity.ok(carService.getCarsByBrandId(brandId));
        }
        if (dealerId != null)
        {
            return ResponseEntity.ok(carService.getCarsByDealerId(dealerId));
        }
        if (fuelType != null)
        {
            return ResponseEntity.ok(carService.getCarsByFuelType(fuelType));
        }
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get car by ID")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long id)
    {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
