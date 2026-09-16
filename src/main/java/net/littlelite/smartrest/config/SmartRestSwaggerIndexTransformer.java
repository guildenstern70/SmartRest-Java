/*
 * SmartREST Project - Java Edition
 * Copyright (c) Alessio Saltarin, 2026
 * This software is licensed under ISC License
 * See LICENSE
 */

package net.littlelite.smartrest.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.core.io.Resource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springdoc.core.utils.Constants.SWAGGER_INITIALIZER_JS;

/**
 * Custom transformer for Swagger UI initialization script that forces a light theme
 * regardless of host system/browser dark mode preferences.
 */
public class SmartRestSwaggerIndexTransformer extends SwaggerIndexPageTransformer
{
    private static final String LIGHT_THEME_SCRIPT = """
        // Force light theme and prevent OS dark-mode preference from enabling dark mode
        try {
          const origMatchMedia = window.matchMedia;
          window.matchMedia = function(query) {
            if (query && query.includes('prefers-color-scheme')) {
              return {
                matches: false,
                media: query,
                onchange: null,
                addListener: function() {},
                removeListener: function() {},
                addEventListener: function() {},
                removeEventListener: function() {},
                dispatchEvent: function() { return false; }
              };
            }
            return origMatchMedia ? origMatchMedia.call(window, query) : { matches: false };
          };
          if (document.documentElement) {
            document.documentElement.classList.remove('dark-mode');
          }
          const observer = new MutationObserver(function() {
            if (document.documentElement && document.documentElement.classList.contains('dark-mode')) {
              document.documentElement.classList.remove('dark-mode');
            }
          });
          observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] });

          const lightStyle = document.createElement('style');
          lightStyle.id = 'smartrest-light-theme';
          lightStyle.textContent = `
            html, body {
              background-color: #f8fafc !important;
              color: #1e293b !important;
            }
            .swagger-ui .topbar {
              background-color: #ffffff !important;
              border-bottom: 1px solid #e2e8f0 !important;
              box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05) !important;
            }
            .swagger-ui .topbar a {
              color: #0f172a !important;
            }
            .swagger-ui .topbar .download-url-wrapper .select-label {
              color: #475569 !important;
            }
            .swagger-ui .topbar .download-url-wrapper input[type=text] {
              border-color: #cbd5e1 !important;
              background: #ffffff !important;
              color: #1e293b !important;
            }
            .swagger-ui .topbar .download-url-wrapper .download-url-button {
              background: #2563eb !important;
            }
            .swagger-ui .dark-mode-toggle {
              display: none !important;
            }
          `;
          document.head.appendChild(lightStyle);
        } catch (e) {
          console.warn('Could not apply light theme', e);
        }
        """;

    public SmartRestSwaggerIndexTransformer(
            SwaggerUiConfigProperties swaggerUiConfig,
            SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            SwaggerWelcomeCommon swaggerWelcomeCommon,
            ObjectMapperProvider objectMapperProvider)
    {
        super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
    }

    @Override
    public Resource transform(HttpServletRequest request, Resource resource,
                              ResourceTransformerChain transformerChain) throws IOException
    {
        final AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean isIndexFound = antPathMatcher.match("**/swagger-ui/**/" + SWAGGER_INITIALIZER_JS, resource.getURL().toString());

        Resource transformed = super.transform(request, resource, transformerChain);
        if (isIndexFound && transformed != null)
        {
            String js = transformed.getContentAsString(StandardCharsets.UTF_8);
            if (js.contains("window.onload = function() {"))
            {
                js = js.replace("window.onload = function() {", "window.onload = function() {\n" + LIGHT_THEME_SCRIPT);
            }
            else
            {
                js = LIGHT_THEME_SCRIPT + "\n" + js;
            }
            return new TransformedResource(resource, js.getBytes(StandardCharsets.UTF_8));
        }
        return transformed;
    }
}
