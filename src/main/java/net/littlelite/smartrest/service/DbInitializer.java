/*
 *  Project SmartREST - Java Edition
 *  Copyright (c) Alessio Saltarin 2022.
 *  This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.service;

import net.littlelite.smartrest.dao.PersonDao;
import net.littlelite.smartrest.dao.PhoneDao;
import net.littlelite.smartrest.model.Person;
import net.littlelite.smartrest.model.Phone;
import net.littlelite.smartrest.model.enums.PersonGroup;
import net.littlelite.smartrest.model.enums.Provider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Service
public class DbInitializer implements ApplicationRunner
{
    protected static final Logger logger = LoggerFactory.getLogger(DbInitializer.class);

    private final PersonDao personDAO;
    private final PhoneDao phoneDao;

    private Random random;

    @Autowired
    public DbInitializer(PersonDao personDAO, PhoneDao phoneDao)
    {
        this.random = new Random();
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
            person.associatePhones(this.getPhones());
        });

        this.personDAO.saveAll(persons);
    }

    @Contract(" -> new")
    private @NotNull List<Phone> getPhones()
    {
        return new ArrayList() {{
            add(createPhone());
            add(createPhone());
        }};
    }

    private @NotNull Phone createPhone()
    {
        var provider = Provider.values()[this.random.nextInt(Provider.values().length)];
        var prefix = this.random.nextInt(333,350);
        long number = this.random.nextLong(2222222222L,9999999999L);
        String phoneNumber = prefix + "-" + number;
        var phone = new Phone();
        phone.setNumber(phoneNumber);
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

