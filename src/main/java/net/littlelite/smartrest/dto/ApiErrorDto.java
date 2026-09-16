/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.dto;

import java.time.Instant;
import java.util.Map;

public record ApiErrorDto(
    Instant timestamp,
    int status,
    String error,
    String message,
    Map<String, String> validationErrors
)
{
    public static ApiErrorDto of(int status, String error, String message)
    {
        return new ApiErrorDto(Instant.now(), status, error, message, null);
    }

    public static ApiErrorDto of(int status, String error, String message, Map<String, String> validationErrors)
    {
        return new ApiErrorDto(Instant.now(), status, error, message, validationErrors);
    }
}
