/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.service;

import lombok.RequiredArgsConstructor;
import net.littlelite.smartrest.dao.BrandRepository;
import net.littlelite.smartrest.dto.BrandDto;
import net.littlelite.smartrest.model.Brand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService
{
    private final BrandRepository brandRepository;

    public List<BrandDto> getAllBrands()
    {
        return brandRepository.findAll()
                .stream()
                .map(BrandDto::fromEntity)
                .toList();
    }

    public Optional<BrandDto> getBrandById(Long id)
    {
        return brandRepository.findById(id)
                .map(BrandDto::fromEntity);
    }

    public Optional<Brand> getBrandEntity(Long id)
    {
        return brandRepository.findById(id);
    }
}
