/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.service;

import lombok.RequiredArgsConstructor;
import net.littlelite.smartrest.dao.BrandRepository;
import net.littlelite.smartrest.dao.CarRepository;
import net.littlelite.smartrest.dao.DealerRepository;
import net.littlelite.smartrest.dto.CarDto;
import net.littlelite.smartrest.dto.CreateDealerDto;
import net.littlelite.smartrest.dto.DealerDto;
import net.littlelite.smartrest.model.Brand;
import net.littlelite.smartrest.model.Dealer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealerService
{
    private final DealerRepository dealerRepository;
    private final CarRepository carRepository;
    private final BrandRepository brandRepository;

    public List<DealerDto> getAllDealers()
    {
        return dealerRepository.findAll()
                .stream()
                .map(DealerDto::fromEntity)
                .toList();
    }

    public Optional<DealerDto> getDealerById(Long id)
    {
        return dealerRepository.findById(id)
                .map(DealerDto::fromEntity);
    }

    public List<CarDto> getCarsByDealerId(Long dealerId)
    {
        return carRepository.findByDealerId(dealerId)
                .stream()
                .map(CarDto::fromEntity)
                .toList();
    }

    @Transactional
    public Dealer createDealer(Dealer dealer)
    {
        if (dealer.getBrands() == null || dealer.getBrands().isEmpty() || dealer.getBrands().size() > 2)
        {
            throw new IllegalArgumentException("A car dealer must have exactly one or two brands.");
        }
        return dealerRepository.save(dealer);
    }

    @Transactional
    public DealerDto createDealer(CreateDealerDto dto)
    {
        if (dto.brandIds() == null || dto.brandIds().isEmpty() || dto.brandIds().size() > 2)
        {
            throw new IllegalArgumentException("A car dealer must have exactly one or two brands.");
        }

        Set<Brand> brands = new HashSet<>();
        for (Long brandId : dto.brandIds())
        {
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + brandId + " not found"));
            brands.add(brand);
        }

        Dealer dealer = Dealer.builder()
                .name(dto.name().trim())
                .city(dto.city().trim())
                .brands(brands)
                .build();

        return DealerDto.fromEntity(dealerRepository.save(dealer));
    }

    @Transactional
    public boolean deleteDealer(Long id)
    {
        Optional<Dealer> dealerOpt = dealerRepository.findById(id);
        if (dealerOpt.isEmpty())
        {
            return false;
        }
        dealerRepository.delete(dealerOpt.get());
        return true;
    }
}
