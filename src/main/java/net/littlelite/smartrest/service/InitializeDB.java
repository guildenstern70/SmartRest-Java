/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.littlelite.smartrest.dao.BrandRepository;
import net.littlelite.smartrest.dao.CarRepository;
import net.littlelite.smartrest.dao.DealerRepository;
import net.littlelite.smartrest.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitializeDB implements CommandLineRunner
{
    private final BrandRepository brandRepository;
    private final DealerRepository dealerRepository;
    private final CarRepository carRepository;

    @Override
    @Transactional
    public void run(String... args)
    {
        populateDatabase();
    }

    @Transactional
    public void populateDatabase()
    {
        if (brandRepository.count() > 0)
        {
            log.info("Database already initialized, skipping seed.");
            return;
        }

        log.info("Initializing database with 5 brands, 5 dealers, and 20 cars...");

        // 1. Create 5 Brands
        Brand bmw = Brand.builder().name("BMW").country("Germany").build();
        Brand audi = Brand.builder().name("Audi").country("Germany").build();
        Brand tesla = Brand.builder().name("Tesla").country("USA").build();
        Brand toyota = Brand.builder().name("Toyota").country("Japan").build();
        Brand porsche = Brand.builder().name("Porsche").country("Germany").build();

        brandRepository.saveAll(List.of(bmw, audi, tesla, toyota, porsche));

        // 2. Create 5 Dealers (each with 1 or 2 brands)
        // Dealer 1: 1 brand
        Dealer bavariaMotors = Dealer.builder()
                .name("Bavaria Motors")
                .city("Milan")
                .brands(Set.of(bmw))
                .build();

        // Dealer 2: 1 brand
        Dealer ingolstadtAuto = Dealer.builder()
                .name("Ingolstadt Premium Auto")
                .city("Turin")
                .brands(Set.of(audi))
                .build();

        // Dealer 3: 1 brand
        Dealer cyberVolt = Dealer.builder()
                .name("CyberVolt Mobility")
                .city("Rome")
                .brands(Set.of(tesla))
                .build();

        // Dealer 4: 2 brands
        Dealer apexGallery = Dealer.builder()
                .name("Apex Performance Gallery")
                .city("Bologna")
                .brands(Set.of(porsche, audi))
                .build();

        // Dealer 5: 2 brands
        Dealer sunriseMotors = Dealer.builder()
                .name("Sunrise Motor Group")
                .city("Verona")
                .brands(Set.of(toyota, bmw))
                .build();

        dealerRepository.saveAll(List.of(bavariaMotors, ingolstadtAuto, cyberVolt, apexGallery, sunriseMotors));

        // 3. Create 20 Cars (distributed across dealers and covering fuel & transmission types)
        List<Car> cars = List.of(
                // Bavaria Motors (BMW)
                Car.builder()
                        .name("M3 Competition")
                        .brand(bmw)
                        .power(510)
                        .zeroToHundredKmh(3.5)
                        .passengers(5)
                        .price(new BigDecimal("102500.00"))
                        .type(FuelType.PETROL)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(bavariaMotors)
                        .build(),
                Car.builder()
                        .name("330d xDrive")
                        .brand(bmw)
                        .power(286)
                        .zeroToHundredKmh(5.1)
                        .passengers(5)
                        .price(new BigDecimal("64200.00"))
                        .type(FuelType.DIESEL)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(bavariaMotors)
                        .build(),
                Car.builder()
                        .name("330e Plug-in Hybrid")
                        .brand(bmw)
                        .power(292)
                        .zeroToHundredKmh(5.8)
                        .passengers(5)
                        .price(new BigDecimal("58900.00"))
                        .type(FuelType.HYBRID_PLUG_IN)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(bavariaMotors)
                        .build(),
                Car.builder()
                        .name("M2 Coupe")
                        .brand(bmw)
                        .power(460)
                        .zeroToHundredKmh(4.2)
                        .passengers(4)
                        .price(new BigDecimal("78500.00"))
                        .type(FuelType.PETROL)
                        .transmission(TransmissionType.MANUAL)
                        .dealer(bavariaMotors)
                        .build(),

                // Ingolstadt Premium Auto (Audi)
                Car.builder()
                        .name("RS6 Avant")
                        .brand(audi)
                        .power(600)
                        .zeroToHundredKmh(3.6)
                        .passengers(5)
                        .price(new BigDecimal("142000.00"))
                        .type(FuelType.PETROL)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(ingolstadtAuto)
                        .build(),
                Car.builder()
                        .name("A6 Avant 50 TDI")
                        .brand(audi)
                        .power(286)
                        .zeroToHundredKmh(5.5)
                        .passengers(5)
                        .price(new BigDecimal("73500.00"))
                        .type(FuelType.DIESEL)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(ingolstadtAuto)
                        .build(),
                Car.builder()
                        .name("Q5 55 TFSI e")
                        .brand(audi)
                        .power(367)
                        .zeroToHundredKmh(5.3)
                        .passengers(5)
                        .price(new BigDecimal("69800.00"))
                        .type(FuelType.HYBRID_PLUG_IN)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(ingolstadtAuto)
                        .build(),
                Car.builder()
                        .name("A3 Sportback")
                        .brand(audi)
                        .power(150)
                        .zeroToHundredKmh(8.4)
                        .passengers(5)
                        .price(new BigDecimal("34500.00"))
                        .type(FuelType.PETROL)
                        .transmission(TransmissionType.MANUAL)
                        .dealer(ingolstadtAuto)
                        .build(),

                // CyberVolt Mobility (Tesla)
                Car.builder()
                        .name("Model 3 Performance")
                        .brand(tesla)
                        .power(510)
                        .zeroToHundredKmh(3.1)
                        .passengers(5)
                        .price(new BigDecimal("57490.00"))
                        .type(FuelType.ELECTRIC)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(cyberVolt)
                        .build(),
                Car.builder()
                        .name("Model Y Long Range")
                        .brand(tesla)
                        .power(384)
                        .zeroToHundredKmh(5.0)
                        .passengers(5)
                        .price(new BigDecimal("49990.00"))
                        .type(FuelType.ELECTRIC)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(cyberVolt)
                        .build(),
                Car.builder()
                        .name("Model S Plaid")
                        .brand(tesla)
                        .power(1020)
                        .zeroToHundredKmh(2.1)
                        .passengers(5)
                        .price(new BigDecimal("109990.00"))
                        .type(FuelType.ELECTRIC)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(cyberVolt)
                        .build(),
                Car.builder()
                        .name("Model X Plaid")
                        .brand(tesla)
                        .power(1020)
                        .zeroToHundredKmh(2.6)
                        .passengers(6)
                        .price(new BigDecimal("114990.00"))
                        .type(FuelType.ELECTRIC)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(cyberVolt)
                        .build(),

                // Apex Performance Gallery (Porsche & Audi)
                Car.builder()
                        .name("911 GT3")
                        .brand(porsche)
                        .power(510)
                        .zeroToHundredKmh(3.4)
                        .passengers(2)
                        .price(new BigDecimal("198000.00"))
                        .type(FuelType.PETROL)
                        .transmission(TransmissionType.MANUAL)
                        .dealer(apexGallery)
                        .build(),
                Car.builder()
                        .name("Taycan Turbo S")
                        .brand(porsche)
                        .power(761)
                        .zeroToHundredKmh(2.8)
                        .passengers(4)
                        .price(new BigDecimal("195000.00"))
                        .type(FuelType.ELECTRIC)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(apexGallery)
                        .build(),
                Car.builder()
                        .name("Panamera 4 E-Hybrid")
                        .brand(porsche)
                        .power(470)
                        .zeroToHundredKmh(4.1)
                        .passengers(4)
                        .price(new BigDecimal("126000.00"))
                        .type(FuelType.HYBRID_PLUG_IN)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(apexGallery)
                        .build(),
                Car.builder()
                        .name("e-tron GT")
                        .brand(audi)
                        .power(530)
                        .zeroToHundredKmh(4.1)
                        .passengers(4)
                        .price(new BigDecimal("108000.00"))
                        .type(FuelType.ELECTRIC)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(apexGallery)
                        .build(),

                // Sunrise Motor Group (Toyota & BMW)
                Car.builder()
                        .name("RAV4 Hybrid")
                        .brand(toyota)
                        .power(222)
                        .zeroToHundredKmh(8.1)
                        .passengers(5)
                        .price(new BigDecimal("41500.00"))
                        .type(FuelType.HYBRID)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(sunriseMotors)
                        .build(),
                Car.builder()
                        .name("GR Yaris")
                        .brand(toyota)
                        .power(280)
                        .zeroToHundredKmh(5.2)
                        .passengers(4)
                        .price(new BigDecimal("46000.00"))
                        .type(FuelType.PETROL)
                        .transmission(TransmissionType.MANUAL)
                        .dealer(sunriseMotors)
                        .build(),
                Car.builder()
                        .name("Prius Plug-in Hybrid")
                        .brand(toyota)
                        .power(223)
                        .zeroToHundredKmh(6.8)
                        .passengers(5)
                        .price(new BigDecimal("43200.00"))
                        .type(FuelType.HYBRID_PLUG_IN)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(sunriseMotors)
                        .build(),
                Car.builder()
                        .name("X5 xDrive30d")
                        .brand(bmw)
                        .power(298)
                        .zeroToHundredKmh(6.1)
                        .passengers(5)
                        .price(new BigDecimal("88500.00"))
                        .type(FuelType.DIESEL)
                        .transmission(TransmissionType.AUTOMATIC)
                        .dealer(sunriseMotors)
                        .build()
        );

        carRepository.saveAll(cars);

        log.info("Database initialized successfully: {} brands, {} dealers, {} cars.",
                brandRepository.count(), dealerRepository.count(), carRepository.count());
    }
}
