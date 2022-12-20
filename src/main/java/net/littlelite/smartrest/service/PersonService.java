/*
 *  Project SmartREST - Java Edition
 *  Copyright (c) Alessio Saltarin 2022.
 *  This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.service;

import net.littlelite.smartrest.dao.PersonDao;
import net.littlelite.smartrest.dto.PersonDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class PersonService
{
    private final Logger logger = LoggerFactory.getLogger(PersonService.class);

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

    public List<PersonDto> getBySurname(String surname)
    {
        var stream = StreamSupport.stream(this.personDAO.findBySurname(surname).spliterator(), false);
        return stream.map(PersonDto::fromEntity).toList();
    }

    public Optional<PersonDto> getById(long id)
    {
        var person = this.personDAO.findById(id);
        return person.map(PersonDto::fromEntity);
    }

    public void deletePerson(long id)
    {
        this.personDAO.deleteById(id);
    }

}
