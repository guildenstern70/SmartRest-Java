/*
 *  Project SmartREST - Java Edition
 *  Copyright (c) Alessio Saltarin 2022.
 *  This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest;

import net.littlelite.smartrest.service.PersonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class PersonServiceTest
{
    @Autowired
    private PersonService personService;

    @DisplayName("Persons should be retrieved")
    @Test
    public void testAdd()
    {
        var persons = this.personService.getAllPersons();
        assertThat(persons).isNotEmpty();
        assertThat(persons.size()).isEqualTo(4);
    }
}
