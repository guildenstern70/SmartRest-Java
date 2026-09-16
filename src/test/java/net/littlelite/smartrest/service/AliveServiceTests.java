/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.service;

import net.littlelite.smartrest.dto.AliveDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AliveServiceTests
{
    @Autowired
    private AliveService aliveService;

    @Test
    @DisplayName("getAliveStatus returns alive=true, version, and database URL")
    void testGetAliveStatus()
    {
        AliveDto aliveDto = aliveService.getAliveStatus();
        assertNotNull(aliveDto);
        assertTrue(aliveDto.alive());
        assertEquals(aliveService.getVersion(), aliveDto.version());
        assertEquals("jdbc:h2:mem:smartrestdb", aliveDto.database());
    }

    @Test
    @DisplayName("sanitizeJdbcUrl removes passwords from various JDBC URL formats")
    void testSanitizeJdbcUrl()
    {
        // Null / blank
        assertEquals("", AliveService.sanitizeJdbcUrl(null));
        assertEquals("", AliveService.sanitizeJdbcUrl("   "));

        // Plain URL without password
        assertEquals("jdbc:h2:mem:smartrestdb",
                AliveService.sanitizeJdbcUrl("jdbc:h2:mem:smartrestdb"));

        // Semicolon separated (H2 / SQL Server)
        assertEquals("jdbc:h2:mem:test;USER=sa",
                AliveService.sanitizeJdbcUrl("jdbc:h2:mem:test;USER=sa;PASSWORD=secret"));
        assertEquals("jdbc:h2:mem:test;USER=sa",
                AliveService.sanitizeJdbcUrl("jdbc:h2:mem:test;PASSWORD=secret;USER=sa"));

        // Query param separated (? and &)
        assertEquals("jdbc:mysql://localhost:3306/db?user=root",
                AliveService.sanitizeJdbcUrl("jdbc:mysql://localhost:3306/db?user=root&password=secret"));
        assertEquals("jdbc:mysql://localhost:3306/db?user=root",
                AliveService.sanitizeJdbcUrl("jdbc:mysql://localhost:3306/db?password=secret&user=root"));
        assertEquals("jdbc:mysql://localhost:3306/db",
                AliveService.sanitizeJdbcUrl("jdbc:mysql://localhost:3306/db?password=secret"));

        // Authority user:password@host (PostgreSQL, etc.)
        assertEquals("jdbc:postgresql://postgres@localhost:5432/mydb",
                AliveService.sanitizeJdbcUrl("jdbc:postgresql://postgres:supersecret@localhost:5432/mydb"));

        // Oracle thin style user/password@host
        assertEquals("jdbc:oracle:thin:scott@localhost:1521:xe",
                AliveService.sanitizeJdbcUrl("jdbc:oracle:thin:scott/tiger@localhost:1521:xe"));
    }
}
