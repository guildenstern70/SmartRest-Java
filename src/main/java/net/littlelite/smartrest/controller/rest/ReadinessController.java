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
import net.littlelite.smartrest.dto.ReadinessDto;
import net.littlelite.smartrest.service.ReadinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ready")
@RequiredArgsConstructor
@Tag(name = "System", description = "Endpoints for system health and status")
public class ReadinessController
{
    private final ReadinessService readinessService;

    @GetMapping
    @Operation(summary = "Kubernetes readiness probe endpoint")
    public ResponseEntity<ReadinessDto> getReadiness()
    {
        ReadinessDto readiness = readinessService.getReadiness();
        if (readiness.ready())
        {
            return ResponseEntity.ok(readiness);
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(readiness);
    }
}
