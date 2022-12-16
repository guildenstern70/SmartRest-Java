/*
 * Project SmartREST - Java Edition
 * Copyright (c) Alessio Saltarin 2022.
 * This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.model;

import jakarta.persistence.*;
import lombok.*;
import net.littlelite.smartrest.model.enums.PersonGroup;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Person
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String email;

    @OneToMany(cascade = CascadeType.ALL,
                fetch = FetchType.EAGER,
                mappedBy = "person")
    private List<Phone> phones = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PersonGroup personGroup;

    public void associatePhones(@NotNull List<Phone> phoneNumbers)
    {
        phoneNumbers.forEach(phone -> {
            phone.setPerson(this);
            this.phones.add(phone);
        });
    }

}
