/*
 *  Project SmartREST - Java Edition
 *  Copyright (c) Alessio Saltarin 2022.
 *  This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.dao;

import net.littlelite.smartrest.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonDao extends CrudRepository<Person, Long>
{
    List<Person> findBySurname(String surname);
    Person findByEmail(String email);
}
