# AI Agents instructions

This is a microservice that provides a skeleton for REST APIs.

It is written in Java 26 with Lombok.

JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-26.jdk/Contents/Home

It uses H2 database in memory.

Use this namespace pattern:

- config: Configuration related classes for Spring Boot
- controller: REST API controllers
- service: Business logic services
- model: Data model classes
- dao: Data access objects as Hibernate repositories
- dto: Serialization classes for data transfer


This template is using Hibernate as code-first approach.
The service 'service/InitializeDB' initializes the database with sample data.

