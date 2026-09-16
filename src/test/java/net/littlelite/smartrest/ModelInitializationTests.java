/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.model;

import net.littlelite.smartrest.dao.BrandRepository;
import net.littlelite.smartrest.dao.CarRepository;
import net.littlelite.smartrest.dao.DealerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ModelInitializationTests
{
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    @DisplayName("Verify that database initializes with 5 brands, 5 dealers, and 20 cars")
    void testInitialCounts()
    {
        assertThat(brandRepository.count()).isEqualTo(5);
        assertThat(dealerRepository.count()).isEqualTo(5);
        assertThat(carRepository.count()).isEqualTo(20);
    }

    @Test
    @DisplayName("Verify that every dealer represents one or two brands")
    void testDealerBrandConstraints()
    {
        List<Dealer> dealers = dealerRepository.findAll();
        for (Dealer dealer : dealers)
        {
            assertThat(dealer.getBrands().size())
                    .as("Dealer '%s' should have 1 or 2 brands", dealer.getName())
                    .isBetween(1, 2);
        }
    }

    @Test
    @DisplayName("Verify that dealer with 0 or >2 brands is rejected")
    void testDealerBrandValidationRule()
    {
        Dealer invalidZeroBrands = Dealer.builder()
                .name("Invalid Dealer 0")
                .city("Rome")
                .brands(Set.of())
                .build();

        assertThatThrownBy(invalidZeroBrands::validateBrands)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("exactly one or two brands");

        Brand b1 = Brand.builder().name("B1").build();
        Brand b2 = Brand.builder().name("B2").build();
        Brand b3 = Brand.builder().name("B3").build();

        Dealer invalidThreeBrands = Dealer.builder()
                .name("Invalid Dealer 3")
                .city("Rome")
                .brands(Set.of(b1, b2, b3))
                .build();

        assertThatThrownBy(invalidThreeBrands::validateBrands)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("exactly one or two brands");
    }

    @Test
    @DisplayName("Verify that all cars have complete specifications and valid fuel types")
    void testCarSpecifications()
    {
        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(20);

        for (Car car : cars)
        {
            assertThat(car.getName()).isNotBlank();
            assertThat(car.getBrand()).isNotNull();
            assertThat(car.getPower()).isPositive();
            assertThat(car.getZeroToHundredKmh()).isPositive();
            assertThat(car.getPassengers()).isPositive();
            assertThat(car.getPrice()).isNotNull();
            assertThat(car.getType()).isNotNull();
            assertThat(car.getTransmission()).isNotNull();
            assertThat(car.getDealer()).isNotNull();

            // Dealer on site must carry the car's brand
            assertThat(car.getDealer().getBrands())
                    .as("Car '%s' brand must be among dealer '%s' brands", car.getName(), car.getDealer().getName())
                    .contains(car.getBrand());
        }

        // Verify fuel types are covered
        Set<FuelType> distinctTypes = cars.stream()
                .map(Car::getType)
                .collect(java.util.stream.Collectors.toSet());

        assertThat(distinctTypes).contains(
                FuelType.PETROL,
                FuelType.DIESEL,
                FuelType.ELECTRIC,
                FuelType.HYBRID,
                FuelType.HYBRID_PLUG_IN
        );
    }
}
