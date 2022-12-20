/*
 *  Project SmartREST - Java Edition
 *  Copyright (c) Alessio Saltarin 2022.
 *  This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.dto;

import net.littlelite.smartrest.model.Phone;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record PhoneDto(String number, String provider)
{
    @Contract("_ -> new")
    public static @NotNull PhoneDto fromEntity(Phone phone)
    {
        return new PhoneDto(phone.getNumber(), phone.getProvider().toString());
    }
}
