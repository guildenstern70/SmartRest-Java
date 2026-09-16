/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.service;

import lombok.extern.slf4j.Slf4j;
import net.littlelite.smartrest.dto.ReadinessDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Service
public class ReadinessService
{
    private final DataSource dataSource;
    private final ApplicationAvailability applicationAvailability;

    public ReadinessService(
            DataSource dataSource,
            @Autowired(required = false) ApplicationAvailability applicationAvailability)
    {
        this.dataSource = dataSource;
        this.applicationAvailability = applicationAvailability;
    }

    public ReadinessDto getReadiness()
    {
        boolean dbOk = isDatabaseReady();
        boolean appOk = isApplicationAcceptingTraffic();

        boolean isReady = dbOk && appOk;
        String status = isReady ? "UP" : "DOWN";
        String dbStatus = dbOk ? "UP" : "DOWN";

        return new ReadinessDto(isReady, status, dbStatus);
    }

    public boolean isDatabaseReady()
    {
        if (dataSource == null)
        {
            return false;
        }
        try (Connection connection = dataSource.getConnection())
        {
            return connection.isValid(2);
        }
        catch (SQLException e)
        {
            log.error("Database readiness check failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isApplicationAcceptingTraffic()
    {
        if (applicationAvailability == null)
        {
            return true;
        }
        return applicationAvailability.getReadinessState() == ReadinessState.ACCEPTING_TRAFFIC;
    }
}
