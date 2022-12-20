/*
 *  Project SmartREST - Java Edition
 *  Copyright (c) Alessio Saltarin 2022.
 *  This software is licensed under MIT License (see LICENSE)
 */

package net.littlelite.smartrest.controller.exceptions;

import org.jetbrains.annotations.NotNull;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(long id)
    {
        super("Could not find resource with id: '" + id + "'");
    }

    public ResourceNotFoundException(String id)
    {
        super("Could not find resource with id: '" + id + "'");
    }

    public ResourceNotFoundException(@NotNull Class<?> entityClass, String id)
    {
        super(entityClass.getSimpleName() + " #" + id + " not found");
    }

    public ResourceNotFoundException(@NotNull Class<?> entityClass, int id)
    {
        super(entityClass.getSimpleName() + " #" + id + " not found");
    }
}


