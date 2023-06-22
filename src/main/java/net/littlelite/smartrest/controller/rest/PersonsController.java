/*
 *  Project SmartREST - Java Edition
 *  Copyright (c) Alessio Saltarin 2022.
 *  This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.controller.rest;

import net.littlelite.smartrest.controller.exceptions.ResourceNotFoundException;
import net.littlelite.smartrest.dto.PersonDto;
import net.littlelite.smartrest.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/api/v1/persons")
public class PersonsController
{
    private final Logger logger = LoggerFactory.getLogger(PersonsController.class);

    private final PersonService personService;

    @Autowired
    public PersonsController(PersonService personService)
    {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAllPersons()
    {
        logger.info("GET all persons");
        return new ResponseEntity<>(this.personService.getAllPersons(), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonById(@PathVariable("id") long id)
    {
        logger.info("DELETE person #" + id);
        this.personService.deletePerson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("findbysurname/{surname}")
    public ResponseEntity<List<PersonDto>> getPersonByNameAndSurname(@PathVariable("surname") String surname)
    {
        logger.info("GET person by surname=" + surname);
        var persons = this.personService.getBySurname(surname);

        if (persons == null)
            throw new ResourceNotFoundException(surname);

        return new ResponseEntity<>(persons, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable("id") long id)
    {
        logger.info("GET person #" + id);
        var person = this.personService.getById(id);

        if (person.isEmpty())
            throw new ResourceNotFoundException(id);

        return new ResponseEntity<>(person.get(), OK);
    }

}
