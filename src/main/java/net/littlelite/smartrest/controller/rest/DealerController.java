/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import net.littlelite.smartrest.dto.CarDto;
import net.littlelite.smartrest.dto.CreateDealerDto;
import net.littlelite.smartrest.dto.DealerDto;
import net.littlelite.smartrest.service.DealerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/dealers")
@RequiredArgsConstructor
@Tag(name = "Car Dealers", description = "Endpoints for car dealers and on-site inventory")
public class DealerController
{
    private final DealerService dealerService;

    @GetMapping
    @Operation(summary = "Get all car dealers")
    public ResponseEntity<List<DealerDto>> getAllDealers()
    {
        return ResponseEntity.ok(dealerService.getAllDealers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get car dealer by ID")
    public ResponseEntity<DealerDto> getDealerById(@PathVariable Long id)
    {
        return dealerService.getDealerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/cars")
    @Operation(summary = "Get cars available on site at this dealership")
    public ResponseEntity<List<CarDto>> getDealerCars(@PathVariable Long id)
    {
        return ResponseEntity.ok(dealerService.getCarsByDealerId(id));
    }

    @PostMapping
    @Operation(summary = "Create a new car dealer")
    public ResponseEntity<DealerDto> createDealer(@Valid @RequestBody CreateDealerDto createDealerDto)
    {
        DealerDto created = dealerService.createDealer(createDealerDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car dealer by ID")
    public ResponseEntity<Void> deleteDealer(@PathVariable Long id)
    {
        if (dealerService.deleteDealer(id))
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
