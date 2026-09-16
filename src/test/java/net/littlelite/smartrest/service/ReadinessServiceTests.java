/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.service;

import net.littlelite.smartrest.dto.ReadinessDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReadinessServiceTests
{
    @Autowired
    private ReadinessService readinessService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ApplicationAvailability applicationAvailability;

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("getReadiness returns UP and ready=true when everything is healthy")
    void testGetReadinessHealthy()
    {
        ReadinessDto dto = readinessService.getReadiness();
        assertTrue(dto.ready());
        assertEquals("UP", dto.status());
        assertEquals("UP", dto.database());
    }

    @Test
    @DisplayName("ReadinessService returns DOWN when DataSource is unavailable")
    void testReadinessWhenDataSourceNull()
    {
        ReadinessService serviceWithNullDb = new ReadinessService(null, applicationAvailability);
        ReadinessDto dto = serviceWithNullDb.getReadiness();
        assertFalse(dto.ready());
        assertEquals("DOWN", dto.status());
        assertEquals("DOWN", dto.database());
    }

    @Test
    @DisplayName("ReadinessService respects ApplicationAvailability state changes")
    void testReadinessStateChanges()
    {
        // Change state to REFUSING_TRAFFIC
        AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.REFUSING_TRAFFIC);
        try
        {
            ReadinessDto dto = readinessService.getReadiness();
            assertFalse(dto.ready());
            assertEquals("DOWN", dto.status());
            assertEquals("UP", dto.database()); // DB is still healthy
        }
        finally
        {
            // Restore to ACCEPTING_TRAFFIC
            AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.ACCEPTING_TRAFFIC);
        }

        // Verify restored
        ReadinessDto restoredDto = readinessService.getReadiness();
        assertTrue(restoredDto.ready());
        assertEquals("UP", restoredDto.status());
    }
}
