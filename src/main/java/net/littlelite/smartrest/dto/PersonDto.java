/*
 * Project SmartREST - Java Edition
 * Copyright (c) Alessio Saltarin 2022.
 * This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.dto;

import net.littlelite.smartrest.model.Person;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record PersonDto(String name, String surname, String email, String group, List<PhoneDto> phones)
{
    @Contract("_ -> new")
    public static @NotNull PersonDto fromEntity(Person person)
    {
        return new PersonDto(
                person.getName(),
                person.getSurname(),
                person.getEmail(),
                person.getPersonGroup().toString(),
                person.getPhones().stream().map(PhoneDto::fromEntity).toList());
    }
}