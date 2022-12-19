/*
 * Project SmartREST - Java Edition
 * Copyright (c) Alessio Saltarin 2022.
 * This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.service;

import net.littlelite.smartrest.dao.PersonDao;
import net.littlelite.smartrest.dto.PersonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class PersonService
{
    private Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonDao personDAO;

    @Autowired
    public PersonService(PersonDao personDAO)
    {
        this.personDAO = personDAO;
    }

    public List<PersonDto> getAllPersons()
    {
        var stream = StreamSupport.stream(this.personDAO.findAll().spliterator(), false);
        return stream.map(PersonDto::fromEntity).toList();
    }

}
