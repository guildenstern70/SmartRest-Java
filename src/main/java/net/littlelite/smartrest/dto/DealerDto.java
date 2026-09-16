/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.dto;

import net.littlelite.smartrest.model.Dealer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record DealerDto(
    Long id,
    String name,
    String city,
    Set<BrandDto> brands,
    int availableCarsCount,
    List<CarDto> cars
)
{
    public static DealerDto fromEntity(Dealer dealer)
    {
        if (dealer == null)
        {
            return null;
        }
        Set<BrandDto> brandDtos = dealer.getBrands() != null
            ? dealer.getBrands().stream().map(BrandDto::fromEntity).collect(Collectors.toSet())
            : Set.of();

        List<CarDto> carDtos = dealer.getCars() != null
            ? dealer.getCars().stream().map(CarDto::fromEntity).toList()
            : List.of();

        return new DealerDto(
            dealer.getId(),
            dealer.getName(),
            dealer.getCity(),
            brandDtos,
            carDtos.size(),
            carDtos
        );
    }
}
