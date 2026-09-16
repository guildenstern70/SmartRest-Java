/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.dto;

public record ReadinessDto(
    boolean ready,
    String status,
    String database
)
{
}
