/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.service;

import lombok.extern.slf4j.Slf4j;
import net.littlelite.smartrest.SmartRest;
import net.littlelite.smartrest.dto.AliveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Service
public class AliveService
{
    private final DataSource dataSource;
    private final BuildProperties buildProperties;

    @Value("${spring.application.version:${application.version:0.0.1-SNAPSHOT}}")
    private String configuredVersion;

    @Value("${spring.datasource.url:}")
    private String configuredDataSourceUrl;

    public AliveService(
            DataSource dataSource,
            @Autowired(required = false) BuildProperties buildProperties)
    {
        this.dataSource = dataSource;
        this.buildProperties = buildProperties;
    }

    public AliveDto getAliveStatus()
    {
        return new AliveDto(true, getVersion(), getDatabaseUrl());
    }

    public String getVersion()
    {
        if (buildProperties != null && buildProperties.getVersion() != null)
        {
            return buildProperties.getVersion();
        }
        if (configuredVersion != null && !configuredVersion.isBlank())
        {
            return configuredVersion;
        }
        String pkgVersion = SmartRest.class.getPackage().getImplementationVersion();
        if (pkgVersion != null && !pkgVersion.isBlank())
        {
            return pkgVersion;
        }
        return "0.0.1-SNAPSHOT";
    }

    public String getDatabaseUrl()
    {
        String url = null;
        if (dataSource != null)
        {
            try (Connection connection = dataSource.getConnection())
            {
                url = connection.getMetaData().getURL();
            }
            catch (SQLException e)
            {
                log.warn("Failed to retrieve JDBC URL from DataSource connection: {}", e.getMessage());
            }
        }

        if (url == null || url.isBlank())
        {
            url = configuredDataSourceUrl;
        }

        return sanitizeJdbcUrl(url);
    }

    public static String sanitizeJdbcUrl(String url)
    {
        if (url == null || url.isBlank())
        {
            return "";
        }

        // Remove password from //user:password@host
        String sanitized = url.replaceAll("(?<=//)([^:@/]+):[^@/]+@", "$1@");

        // Remove Oracle thin user/password@host
        sanitized = sanitized.replaceAll("(?<=:)([a-zA-Z0-9_]+)/[^@]+@", "$1@");

        // Remove semicolon parameters: ;password=... or ;pwd=...
        sanitized = sanitized.replaceAll("(?i);(password|pwd)=[^;]*", "");

        // Remove query parameters: ?password=... or &password=...
        sanitized = sanitized.replaceAll("(?i)(?<=[?&])(password|pwd)=[^&;]*&?", "");

        // Clean up any trailing ? or &
        sanitized = sanitized.replaceAll("[?&]$", "");

        return sanitized;
    }
}
