/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.dao;

import net.littlelite.smartrest.model.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long>
{
    List<Dealer> findByCityIgnoreCase(String city);
}
