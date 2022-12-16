/*
 * Project SmartREST - Java Edition
 * Copyright (c) Alessio Saltarin 2022.
 * This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.model;

import jakarta.persistence.*;
import lombok.Data;
import net.littlelite.smartrest.model.enums.Provider;

@Entity
@Data
public class Phone
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private Provider provider;

    @ManyToOne
    private Person person;
}
