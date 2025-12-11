# Mini Shop

## Project Description

Mini Shop is a comprehensive e-commerce backend application built with Spring Boot and Java. It provides a complete REST API for managing an online shop with features including product catalog management, user authentication, order processing, and file storage. The platform is designed with security, scalability, and maintainability in mind, utilizing modern Java frameworks and cloud-native deployment options.

## Features

- **Product Catalog Management**: Create, read, update, and delete products with categories, pricing, and stock management
- **Product Categories**: Organize products into categories with unique naming constraints
- **User Management**: User registration, profile management, and role-based access control
- **Authentication & Authorization**: JWT-based authentication with secure token handling and role-based authorization
- **Order Management**: Complete order lifecycle management with order status tracking
- **File Storage Integration**: Upload and manage product images using MinIO object storage

## Technology Stack

### Backend

- **Language**: Java 17
- **Framework**: Spring Boot 3.5.6
- **Build Tool**: Maven
- **Package Manager**: Maven Central

### Database & ORM

- **Database**: MySQL
- **ORM**: Hibernate
- **Migration Tool**: Flyway
- **Connection Pool**: HikariCP (default with Spring Boot)

### File Storage

- **Object Storage**: MinIO 8.5.7

### Testing

- **Framework**: JUnit 5 (via Spring Boot Test)
- **Mocking**: Mockito
