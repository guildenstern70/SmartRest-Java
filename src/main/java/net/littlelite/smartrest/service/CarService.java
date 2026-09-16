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
import net.littlelite.smartrest.dto.CreateCarDto;
import net.littlelite.smartrest.model.Brand;
import net.littlelite.smartrest.model.Car;
import net.littlelite.smartrest.model.Dealer;
import net.littlelite.smartrest.model.FuelType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService
{
    private final CarRepository carRepository;
    private final BrandRepository brandRepository;
    private final DealerRepository dealerRepository;

    public List<CarDto> getAllCars()
    {
        return carRepository.findAll()
                .stream()
                .map(CarDto::fromEntity)
                .toList();
    }

    public Optional<CarDto> getCarById(Long id)
    {
        return carRepository.findById(id)
                .map(CarDto::fromEntity);
    }

    public List<CarDto> getCarsByBrandId(Long brandId)
    {
        return carRepository.findByBrandId(brandId)
                .stream()
                .map(CarDto::fromEntity)
                .toList();
    }

    public List<CarDto> getCarsByDealerId(Long dealerId)
    {
        return carRepository.findByDealerId(dealerId)
                .stream()
                .map(CarDto::fromEntity)
                .toList();
    }

    public List<CarDto> getCarsByFuelType(FuelType type)
    {
        return carRepository.findByType(type)
                .stream()
                .map(CarDto::fromEntity)
                .toList();
    }

    @Transactional
    public CarDto createCar(CreateCarDto dto)
    {
        Brand brand = brandRepository.findById(dto.brandId())
                .orElseThrow(() -> new IllegalArgumentException("Brand with ID " + dto.brandId() + " not found"));

        Dealer dealer = null;
        if (dto.dealerId() != null)
        {
            dealer = dealerRepository.findById(dto.dealerId())
                    .orElseThrow(() -> new IllegalArgumentException("Dealer with ID " + dto.dealerId() + " not found"));

            if (!dealer.getBrands().contains(brand))
            {
                throw new IllegalArgumentException("Dealer '" + dealer.getName()
                        + "' does not carry brand '" + brand.getName() + "'");
            }
        }

        Car car = Car.builder()
                .name(dto.name().trim())
                .brand(brand)
                .power(dto.power())
                .zeroToHundredKmh(dto.zeroToHundredKmh())
                .passengers(dto.passengers())
                .price(dto.price())
                .type(dto.type())
                .transmission(dto.transmission())
                .dealer(dealer)
                .build();

        return CarDto.fromEntity(carRepository.save(car));
    }

    @Transactional
    public boolean deleteCar(Long id)
    {
        Optional<Car> carOptional = carRepository.findById(id);
        if (carOptional.isEmpty())
        {
            return false;
        }

        carRepository.delete(carOptional.get());
        return true;
    }
}
