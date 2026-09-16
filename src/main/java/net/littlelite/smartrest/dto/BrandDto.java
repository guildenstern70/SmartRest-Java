/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.dto;

import net.littlelite.smartrest.model.Brand;

public record BrandDto(
    Long id,
    String name,
    String country
)
{
    public static BrandDto fromEntity(Brand brand)
    {
        if (brand == null)
        {
            return null;
        }
        return new BrandDto(brand.getId(), brand.getName(), brand.getCountry());
    }
}
