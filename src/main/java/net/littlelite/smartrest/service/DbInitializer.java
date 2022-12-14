/*
 * Project SmartREST - Java Edition
 * Copyright (c) Alessio Saltarin 2022.
 * This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.service;

import net.littlelite.smartrest.dao.PersonDao;
import net.littlelite.smartrest.model.PersonGroup;
import net.littlelite.smartrest.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;


@Service
public class DbInitializer implements ApplicationRunner
{
    protected static final Logger logger = LoggerFactory.getLogger(DbInitializer.class);

    private PersonDao personDAO;

    @Autowired
    public DbInitializer(PersonDao personDAO)
    {
        this.personDAO = personDAO;
    }

    @Override
    public void run(ApplicationArguments args)
    {
        logger.info("Populating DB...");

        Person[] persons = new Person[]{
                this.createPerson("Alessio", "Saltarin",
                        "alessiosaltarin@gmail.com", PersonGroup.ADMINISTRATOR),
                this.createPerson("Laura", "Renzi",
                        "laurarenzi@gmail.com", PersonGroup.USER),
                this.createPerson("Elena", "Santandrea",
                        "elenasan@gmail.com", PersonGroup.USER),
                this.createPerson("Filippo", "Giusti",
                        "filippogiuisti@outlook.com", PersonGroup.GUEST)
        };

        logger.info("Done populating DB.");

    }

    private Person createPerson(String name, String surname, String email, PersonGroup group)
    {
        var person = new Person();
        person.setName(name);
        person.setSurname(surname);
        person.setEmail(email);
        person.setPersonGroup(group);
        return person;
    }
}

