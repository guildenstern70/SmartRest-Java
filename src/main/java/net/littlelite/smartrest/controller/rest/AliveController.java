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
import net.littlelite.smartrest.dto.AliveDto;
import net.littlelite.smartrest.service.AliveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alive")
@RequiredArgsConstructor
@Tag(name = "System", description = "Endpoints for system health and status")
public class AliveController
{
    private final AliveService aliveService;

    @GetMapping
    @Operation(summary = "Get application status, version, and database URL")
    public ResponseEntity<AliveDto> getAlive()
    {
        return ResponseEntity.ok(aliveService.getAliveStatus());
    }
}
