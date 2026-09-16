/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.dao;

import net.littlelite.smartrest.model.Car;
import net.littlelite.smartrest.model.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>
{
    List<Car> findByBrandId(Long brandId);

    List<Car> findByDealerId(Long dealerId);

    List<Car> findByType(FuelType type);
}
