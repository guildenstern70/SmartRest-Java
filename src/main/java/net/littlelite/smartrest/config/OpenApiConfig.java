/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import net.littlelite.smartrest.service.AliveService;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Configuration for OpenAPI 3 documentation and Swagger UI customization.
 */
@Configuration
public class OpenApiConfig
{
    private static final String API_TITLE = "SmartREST API";
    private static final String API_DESCRIPTION = "Microservice REST API skeleton";
    private static final String AUTHOR_NAME = "Alessio Saltarin";
    private static final String LICENSE_NAME = "ISC";
    private static final String LICENSE_URL = "https://opensource.org/licenses/ISC";

    @Bean
    public OpenAPI smartRestOpenAPI(AliveService aliveService)
    {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .version(aliveService.getVersion())
                        .description(API_DESCRIPTION)
                        .contact(new Contact()
                                .name(AUTHOR_NAME))
                        .license(new License()
                                .name(LICENSE_NAME)
                                .url(LICENSE_URL)));
    }

    @Bean
    @Lazy(false)
    public SwaggerIndexTransformer indexPageTransformer(
            SwaggerUiConfigProperties swaggerUiConfig,
            SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            SwaggerWelcomeCommon swaggerWelcomeCommon,
            ObjectMapperProvider objectMapperProvider)
    {
        return new SmartRestSwaggerIndexTransformer(
                swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
    }
}
