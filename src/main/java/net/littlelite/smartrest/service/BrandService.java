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

    @Transactional
    public BrandDto createBrand(net.littlelite.smartrest.dto.CreateBrandDto dto)
    {
        String trimmedName = dto.name().trim();
        if (brandRepository.findByNameIgnoreCase(trimmedName).isPresent())
        {
            throw new IllegalArgumentException("Brand with name '" + trimmedName + "' already exists");
        }

        Brand brand = Brand.builder()
                .name(trimmedName)
                .country(dto.country().trim())
                .build();

        return BrandDto.fromEntity(brandRepository.save(brand));
    }

    @Transactional
    public boolean deleteBrand(Long id)
    {
        Optional<Brand> brandOptional = brandRepository.findById(id);
        if (brandOptional.isEmpty())
        {
            return false;
        }

        Brand brand = brandOptional.get();
        if (brand.getDealers() != null && !brand.getDealers().isEmpty())
        {
            List<String> dealerNames = brand.getDealers().stream()
                    .map(net.littlelite.smartrest.model.Dealer::getName)
                    .toList();
            throw new IllegalStateException("Cannot delete brand '" + brand.getName()
                    + "' because it is associated with dealer(s): " + dealerNames);
        }

        brandRepository.delete(brand);
        return true;
    }
}
