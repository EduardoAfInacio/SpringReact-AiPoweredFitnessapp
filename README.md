# SpringReact-AiPoweredFitnessapp

An AI-powered fitness application built with Spring Boot microservices, React frontend, and integrated with message queuing for real-time activity processing and AI-driven fitness recommendations.

## Overview

- **Goal**: Demonstrate a modern microservices architecture combined with AI capabilities for comprehensive fitness tracking and personalized workout recommendations
- **Implementation**: Leverages Spring Boot microservices, RabbitMQ for asynchronous messaging, MongoDB and PostgreSQL for data persistence, and React for interactive frontend
- **Features**: User management, activity tracking, AI-powered fitness recommendations, real-time notifications, and secure authentication with Keycloak
- **AI Model** : The AI MicroService uses the **Gemini 2.5 Flash** model to generate personalized and context-aware fitness recommendations based on user activity data.

## Project Structure

### Backend Microservices

- **`UserMicroService`** — User management, authentication, and profile management

  - PostgreSQL database integration
  - Spring Data JPA for ORM
  - User validation and registration endpoints
- **`ActivityMicroService`** — Activity tracking, workout logging, and history

  - MongoDB for flexible activity data storage
  - RabbitMQ integration for asynchronous message processing
  - REST endpoints for activity management
- **`AiMicroService`** — AI-powered fitness recommendations and analysis

  - MongoDB for ML model data and analytics
  - RabbitMQ consumer for activity events
  - Advanced fitness recommendations engine
  - WebFlux for reactive programming
  - Integration with Google Gemini 2.5 Flash API for AI-generated recommendations
- **`api-gateway`** — Central entry point for all client requests

  - Spring Cloud Gateway for routing and load balancing
  - OAuth2 resource server with Keycloak integration
  - Request/response filtering and security policies
- **`eurekaserver`** — Service discovery and registration

  - Netflix Eureka server for microservice registration
  - Centralized service location management
- **`configuration-server`** — Centralized configuration management

  - Spring Cloud Config server
  - Dynamic configuration updates for all microservices

### Frontend

- **`react-frontend`** — Modern React web application
  - Vite for fast development and optimized builds
  - Material-UI (MUI) for responsive UI components
  - Redux Toolkit for state management
  - OAuth2 authentication with PKCE flow
  - React Router for navigation

### Infrastructure

- **`docker/`** — Docker Compose orchestration
  - PostgreSQL 15 (relational database)
  - MongoDB (NoSQL database)
  - RabbitMQ (message broker)
  - Keycloak (identity and access management)

## Technologies

### Backend

- Java 17
- Spring Boot 3.5.6 / 3.5.7
- Spring Cloud 2025.0.0
- Spring Data JPA (User Service)
- Spring Data MongoDB (Activity & AI Services)
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka
- Spring Cloud Config
- Spring AMQP (RabbitMQ integration)
- Spring WebFlux (reactive programming)
- Spring OAuth2 Resource Server
- Lombok (annotations and boilerplate reduction)
- Maven

### Frontend

- React 19.2
- Vite 7.2
- Material-UI 7.3.5
- Redux Toolkit 2.11
- React Router 7.9
- OAuth2 Code PKCE authentication

### Infrastructure & Messaging

- RabbitMQ (message broker and queuing)
- PostgreSQL 15 (relational data)
- MongoDB (document database)
- Keycloak 26.0 (identity management)
- Docker & Docker Compose

## Architecture Overview

### Microservices Communication Flow

```
React Frontend
    ↓
API Gateway (OAuth2 Security)
    ↓
┌─────────────────────────────────────┐
│   User MicroService                 │
│   (PostgreSQL)                       │
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│   Activity MicroService             │
│   (MongoDB) → RabbitMQ              │
└─────────────────────────────────────┘
    ↓ (async messages)
┌─────────────────────────────────────┐
│   AI MicroService                   │
│   (MongoDB) - Processes activities  │
│   and generates recommendations     │
└─────────────────────────────────────┘

Service Discovery: Eureka Server
Config Management: Configuration Server
Authentication: Keycloak
```

### Message Queue Architecture

1. User records fitness activity → Activity Service
2. Activity Service publishes message to RabbitMQ
3. AI Service consumes message asynchronously
4. AI Service processes activity data
5. Recommendations generated (via Gemini 2.5 Flash) and stored
6. Frontend notified of updates via API

## Requirements

- **JDK 17** or higher
- **Maven 3.8+**
- **Node.js 18+** (for React frontend)
- **Docker & Docker Compose** (for infrastructure services)

## How to Run

### 1. Start Infrastructure Services

Start all required services using Docker Compose:

```bash
cd docker
docker-compose up -d
```

This will initialize:

- PostgreSQL database for user service
- MongoDB for activity and AI services
- RabbitMQ message broker
- Keycloak authentication server

### 2. Configure Keycloak (One-time setup)

1. Access Keycloak at `http://localhost:8181`
2. Login with credentials: `admin` / `admin123`
3. Create a new realm for the fitness app
4. Configure OAuth2 client settings
5. Create users and assign roles

### 3. Start Configuration Server

```bash
cd configuration-server
./mvnw spring-boot:run
```

The configuration server runs on `http://localhost:8888`

### 4. Start Eureka Server

```bash
cd eurekaserver
./mvnw spring-boot:run
```

Eureka dashboard available at `http://localhost:8761`

### 5. Start Microservices

Start each microservice in separate terminals:

**User MicroService:**

```bash
cd UserMicroService
./mvnw spring-boot:run
```

**Activity MicroService:**

```bash
cd ActivityMicroService
./mvnw spring-boot:run
```

**AI MicroService:**

```bash
cd AiMicroService
./mvnw spring-boot:run
```

**API Gateway:**

```bash
cd api-gateway
./mvnw spring-boot:run
```

### 6. Start React Frontend

```bash
cd react-frontend
npm install
npm run dev
```

The frontend will be available at `http://localhost:5173`

### 7. Run Tests

For each microservice:

```bash
cd <MicroService>
./mvnw test
```

### Eureka Service Discovery

- `http://localhost:8761` - Eureka Dashboard

### RabbitMQ Management

- `http://localhost:15672` - RabbitMQ Management Console
- Default credentials: `admin` / `123`

## Configuration

### Database Configuration

Each service has its own database configuration:

**User Service (application.properties):**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fitnessdb
spring.datasource.username=admin
spring.datasource.password=123
spring.jpa.hibernate.ddl-auto=update
```

**Activity & AI Services (application.yaml):**

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://admin:123@localhost:27017/fitnessdb?authSource=admin
```

### RabbitMQ Configuration

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: 123
```

### OAuth2/Keycloak Configuration

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8181/realms/fitness
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8181/realms/fitness/protocol/openid-connect/certs
```

## Development Environment Setup

### Prerequisites

1. Java 17 installed and set in PATH
2. Maven 3.8+ installed
3. Node.js 18+ installed
4. Docker installed
5. Git installed

## Key Features

### User Management

- User registration and authentication
- Profile management and preferences
- Role-based access control via Keycloak
- Secure password handling

### Activity Tracking

- Real-time activity logging
- Multiple activity types support
- Workout history and analytics
- Asynchronous processing for performance

### AI-Powered Recommendations

- Personalized fitness recommendations
- Activity-based analysis
- Performance tracking
- Predictive insights
- Real-time recommendations via async messaging

### Real-time Communication

- RabbitMQ-based async messaging
- Event-driven architecture
- Scalable message processing
- Decoupled service communication

### Security

- OAuth2 authentication with PKCE flow
- JWT token validation
- API Gateway security filters
- Keycloak integration for centralized auth

### Service Discovery & Configuration

- Eureka for dynamic service registration
- Centralized configuration management
- Spring Cloud Config integration
- Dynamic property updates
