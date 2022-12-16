/*
 * Project SmartREST - Java Edition
 * Copyright (c) Alessio Saltarin 2022.
 * This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.dao;

import net.littlelite.smartrest.model.Phone;
import net.littlelite.smartrest.model.enums.Provider;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhoneDao extends CrudRepository<Phone, Long>
{
    List<Phone> findByProvider(Provider provider);
}

