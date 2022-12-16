/*
 * Project SmartREST - Java Edition
 * Copyright (c) Alessio Saltarin 2022.
 * This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.service;

import net.littlelite.smartrest.dao.PersonDao;
import net.littlelite.smartrest.dao.PhoneDao;
import net.littlelite.smartrest.model.Phone;
import net.littlelite.smartrest.model.enums.PersonGroup;
import net.littlelite.smartrest.model.Person;
import net.littlelite.smartrest.model.enums.Provider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service
public class DbInitializer implements ApplicationRunner
{
    protected static final Logger logger = LoggerFactory.getLogger(DbInitializer.class);

    private final PersonDao personDAO;
    private final PhoneDao phoneDao;

    @Autowired
    public DbInitializer(PersonDao personDAO, PhoneDao phoneDao)
    {
        this.personDAO = personDAO;
        this.phoneDao = phoneDao;
    }

    @Override
    public void run(ApplicationArguments args)
    {
        this.populateDatabase();
        var numberOfPhones = this.phoneDao.count();
        var numberOfPersons = this.personDAO.count();
        logger.info("Done populating DB: " + numberOfPersons + " persons and " + numberOfPhones + " phones.");
    }

    @Transactional
    protected void populateDatabase()
    {
        logger.info("Populating DB...");

        List<Phone> phones = Arrays.asList(
                this.createPhone("348-39020292", Provider.TIM),
                this.createPhone("333-32232211", Provider.VODAFONE));

        List<Person> persons = Arrays.asList(
                this.createPerson("Alessio", "Saltarin",
                        "alessiosaltarin@gmail.com", PersonGroup.ADMINISTRATOR),
                this.createPerson("Laura", "Renzi",
                        "laurarenzi@gmail.com", PersonGroup.USER),
                this.createPerson("Elena", "Santandrea",
                        "elenasan@gmail.com", PersonGroup.USER),
                this.createPerson("Filippo", "Giusti",
                        "filippogiuisti@outlook.com", PersonGroup.GUEST)
        );

        persons.forEach(person -> {
            person.associatePhones(phones);
        });

        this.personDAO.saveAll(persons);
    }

    private @NotNull Phone createPhone(String number, Provider provider)
    {
        var phone = new Phone();
        phone.setNumber(number);
        phone.setProvider(provider);
        return phone;
    }

    private @NotNull Person createPerson(String name, String surname, String email, PersonGroup group)
    {
        var person = new Person();
        person.setName(name);
        person.setSurname(surname);
        person.setEmail(email);
        person.setPersonGroup(group);
        return person;
    }
}

