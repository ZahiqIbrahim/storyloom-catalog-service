# 📚🎬 Storyloom Catalog Service

A Spring Boot microservice that serves as the **catalog backbone** of the Storyloom platform. It aggregates book and movie data from external APIs — [Open Library](https://openlibrary.org) and [The Movie Database (TMDB)](https://www.themoviedb.org/) — and persists trending items locally in PostgreSQL for fast retrieval.

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [API Reference](#-api-reference)
  - [Books Endpoints](#books-endpoints)
  - [Movies Endpoints](#movies-endpoints)
- [Project Structure](#-project-structure)
- [Data Models](#-data-models)
- [Scheduled Tasks](#-scheduled-tasks)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [Environment Variables](#-environment-variables)
- [External API Integrations](#-external-api-integrations)
- [Service Discovery & Communication](#-service-discovery--communication)
- [Security Notes](#-security-notes)

---

## 🔎 Overview

Storyloom Catalog Service is a RESTful microservice responsible for:

1. **Searching books and movies** in real time by querying the Open Library and TMDB APIs via OpenFeign clients.
2. **Caching trending books and movies** in a PostgreSQL database, automatically refreshed on a weekly schedule.
3. **Registering with Eureka** for service discovery within the Storyloom microservices ecosystem.
4. **Communicating with other services** (e.g., the Auth Service) through declarative Feign interfaces.

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────────────┐
│                   Storyloom Ecosystem                   │
│                                                         │
│  ┌──────────────┐     ┌──────────────────────┐          │
│  │   Eureka     │◄────│  Catalog Service     │          │
│  │  (Registry)  │     │    (This Service)    │          │
│  └──────┬───────┘     └──────┬───────────────┘          │
│         │                    │                          │
│         │              ┌─────┴─────┐                    │
│         │              │ PostgreSQL │                   │
│         │              │   (DB)    │                    │
│         │              └───────────┘                    │
│  ┌──────┴───────┐     ┌─────┴──────────────────────┐     │
│  │ Auth Service  │◄────│     Feign Clients          │     │
│  └──────────────┘     │  ┌──────────┐ ┌─────────┐ │     │
│                        │  │  Open     │ │  TMDB   │ │     │
│                        │  │  Library  │ │  API    │ │     │
│                        │  └──────────┘ └─────────┘ │     │
│                        └───────────────────────────┘     │
└─────────────────────────────────────────────────────────┘
```

---

## 🛠 Tech Stack

| Technology              | Version   | Purpose                              |
|-------------------------|-----------|--------------------------------------|
| **Java**                | 21        | Runtime language                     |
| **Spring Boot**         | 4.0.6     | Application framework                |
| **Spring Cloud**        | 2025.1.1  | Microservices tooling                |
| **Spring Data JPA**     | —         | Database persistence layer           |
| **Spring Cloud OpenFeign** | —     | Declarative HTTP client (external & inter-service) |
| **Netflix Eureka Client** | —      | Service registration & discovery     |
| **PostgreSQL**          | —         | Relational database                   |
| **Lombok**              | —         | Boilerplate reduction (getters, setters, etc.) |
| **Maven**               | —         | Build & dependency management         |

---

## ✨ Features

### 🔍 Real-Time Search
- Search for **individual books or movies** by title — results are fetched live from external APIs.
- Search for **multiple books or movies** in a single request.

### 📈 Trending Content
- Weekly **trending books** (sourced from Open Library's trending endpoint).
- Weekly **trending movies** (sourced from TMDB's trending endpoint, up to 3 pages / ~60 movies).
- Trending data is persisted in PostgreSQL and served from the database for fast responses.

### ⏰ Automatic Weekly Refresh
- A **scheduled cron job** runs every Monday at midnight (IST) to:
  - Clear existing trending data from the database.
  - Fetch the latest trending books and movies from their respective APIs.
  - Store the fresh data in PostgreSQL.

### 🌐 Microservice Integration
- Registers itself with **Eureka** for dynamic service discovery.
- Communicates with sibling services (e.g., **Auth Service**) via **Feign** interfaces.
- Configurable **retry mechanism** on Feign client requests (3 attempts with backoff).

---

## 📡 API Reference

All endpoints are prefixed with `http://localhost:8090`.

### Books Endpoints

#### Search a Single Book
```
POST /books/getBook
Content-Type: text/plain

Body: "Clean Code"
```
> Searches the Open Library API and returns the top matching book.

#### Search Multiple Books
```
POST /books/getBooks
Content-Type: application/json

Body: ["Clean Code", "The Pragmatic Programmer", "Design Patterns"]
```
> Searches for each book individually and returns a list of results.

#### Get Trending Books
```
POST /books/getTrendingBooks
```
> Returns the weekly trending books stored in the local database.

---

### Movies Endpoints

#### Search a Single Movie
```
POST /movies/getMovie
Content-Type: text/plain

Body: "Inception"
```
> Searches the TMDB API and returns the top matching movie.

#### Search Multiple Movies
```
POST /movies/getMovies
Content-Type: application/json

Body: ["Inception", "Interstellar", "The Dark Knight"]
```
> Searches for each movie individually and returns a list of results. Failed lookups are silently skipped.

#### Get Trending Movies
```
POST /movies/getTrendingMovies
```
> Returns the weekly trending movies stored in the local database.

---

### Error Responses

All endpoints return consistent error responses on failure:

```json
{
  "Error": "<error message>"
}
```

---

## 📁 Project Structure

```
src/main/java/com/example/storyloom_catalog_service/
│
├── StoryloomCatalogServiceApplication.java   # Entry point & scheduled task
├── AuthInterface.java                         # Feign client for Auth Service
│
├── config/
│   └── feignConfig.java                       # Feign retry configuration
│
├── controller/
│   ├── BooksController.java                   # Book REST endpoints
│   └── MoviesController.java                  # Movie REST endpoints
│
├── external_clients/
│   ├── OpenLibraryClient.java                 # Feign client → Open Library API
│   └── TmdbClient.java                        # Feign client → TMDB API
│
├── model/
│   ├── Book.java                              # Book JPA entity
│   └── Movie.java                             # Movie JPA entity
│
├── repo/
│   ├── BooksRepo.java                         # Book JPA repository
│   └── MoviesRepo.java                        # Movie JPA repository
│
└── service/
    ├── BooksService.java                      # Book business logic
    └── MovieService.java                      # Movie business logic
```

---

## 🗃 Data Models

### Book (`books` table)

| Field              | Type         | Description                                     |
|--------------------|-------------|-------------------------------------------------|
| `id`               | Long (PK)   | Auto-generated primary key                      |
| `title`            | String      | Book title                                      |
| `subtitle`         | String (TEXT) | Book subtitle                                  |
| `authorName`       | List<String> | List of author names                            |
| `firstPublishYear` | String      | Year of first publication                       |
| `key`              | String      | Open Library resource key (e.g., `/works/OL...`) |
| `cover`            | String      | URL to the book cover image                      |

### Movie (`Movies` table)

| Field          | Type         | Description                                     |
|----------------|-------------|-------------------------------------------------|
| `id`           | Long (PK)   | Auto-generated primary key                      |
| `title`        | String      | Movie title                                      |
| `overview`     | String (TEXT) | Plot synopsis                                  |
| `posterPath`   | String      | Full URL to the movie poster (TMDB image CDN)    |
| `releaseDate`  | String      | Release date (e.g., `2022-03-01`)               |
| `voteAverage`  | String      | Average user vote score (e.g., `7.661`)          |

---

## ⏰ Scheduled Tasks

| Task                    | Schedule                              | Timezone     | Description                                              |
|-------------------------|---------------------------------------|--------------|----------------------------------------------------------|
| `updateDb()`            | Every Monday at `00:00`               | Asia/Kolkata | Fetches trending books & movies from external APIs and replaces all existing DB records. |

**Cron expression:** `0 0 0 * * MON`

---

## 🚀 Getting Started

### Prerequisites

- **Java 21+** installed and configured
- **Maven** (or use the included `mvnw` wrapper)
- **PostgreSQL** running locally on port `5432`
- (Optional) **Eureka Server** running for service discovery

### Configuration

1. **Create the database** in PostgreSQL:

   ```sql
   CREATE DATABASE "storyloom-catalog-db";
   ```

2. **Configure `application.properties`** (located at `src/main/resources/application.properties`):

   ```properties
   spring.application.name=storyloom-catalog-service
   server.port=8090

   # Database
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.datasource.url=jdbc:postgresql://localhost:5432/storyloom-catalog-db
   spring.datasource.username=postgres
   spring.datasource.password=<your-password>

   # JPA
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

   # TMDB API Key — get yours at https://developer.themoviedb.org
   tmdb.api.key=<your-tmdb-api-key>
   ```

   > 💡 For best practices, use environment variables instead of hardcoding secrets. See [Environment Variables](#-environment-variables).

### Running the Application

**Using Maven Wrapper:**

```bash
# Unix / macOS / Git Bash
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

**Using Maven:**

```bash
mvn spring-boot:run
```

**Build JAR and run:**

```bash
mvn clean package
java -jar target/storyloom-catalog-service-0.0.1-SNAPSHOT.jar
```

The service will start on **port 8090** and automatically register with Eureka (if available).

---

## 🔑 Environment Variables

You can externalize sensitive configuration using environment variables instead of hardcoding them in `application.properties`:

| Variable                  | Description                        | Default                           |
|---------------------------|------------------------------------|-----------------------------------|
| `SPRING_DATASOURCE_PASSWORD` | PostgreSQL password             | —                                 |
| `TMDB_API_KEY`            | TMDB API key                       | —                                 |
| `EUREKA_DEFAULT_ZONE`     | Eureka server URL                  | `http://localhost:8761/eureka`    |

Example usage in `application.properties`:

```properties
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
tmdb.api.key=${TMDB_API_KEY}
```

---

## 🔌 External API Integrations

### Open Library

| Endpoint                     | Purpose                     |
|------------------------------|-----------------------------|
| `GET /search.json?q={title}&limit=1` | Search books by title     |
| `GET /trending/weekly.json`  | Get weekly trending books   |

- **Base URL:** `https://openlibrary.org`
- **Authentication:** None required (public API)
- **Client:** `OpenLibraryClient.java` (Feign)

### TMDB (The Movie Database)

| Endpoint                               | Purpose                        |
|----------------------------------------|--------------------------------|
| `GET /search/movie?query={title}&api_key={key}` | Search movies by title    |
| `GET /trending/movie/week?api_key={key}&page={n}` | Get weekly trending movies |

- **Base URL:** `https://api.themoviedb.org/3`
- **Authentication:** API key (query parameter)
- **Client:** `TmdbClient.java` (Feign)

---

## 🌐 Service Discovery & Communication

- **Eureka Client:** The service auto-registers with a Eureka server at startup via `spring-cloud-starter-netflix-eureka-client`.
- **Feign Inter-Service Calls:** The `AuthInterface` Feign client enables communication with the `STORYLOOM-AUTH-SERVICE` for authentication-related operations.
- **Retry Policy:** Configured via `feignConfig.java` — initial interval `100ms`, max interval `1000ms`, up to **3 retry attempts** on failure.

---

## 🔒 Security Notes

> ⚠️ **Important:** The current `application.properties` contains hardcoded database credentials and API keys. Before deploying to any shared or production environment:

1. **Never commit secrets to version control.** Add `application.properties` (or a secrets file) to `.gitignore`.
2. **Use environment variables** or a **secrets manager** (e.g., Spring Cloud Config, HashiCorp Vault) for sensitive values.
3. **Rotate API keys** if they have been exposed in a public repository.
4. Consider adding **authentication and authorization** to the REST endpoints (e.g., Spring Security, JWT filters).

---

## 📄 License

This project is part of the **Storyloom** platform. All rights reserved.

---

*Built with ☕ and Spring Boot*
