# Match My Coffee – Backend ☕

![Backend - Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=FFFFFF)
![Java 21](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=FFFFFF)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=FFFFFF)

Backend service for the **Match My Coffee** webshop This service exposes REST APIs for products, orders, content, and integrates with PostgreSQL, Firebase Auth, and an external Python AI agent.

---

## Architecture

**Tech stack**

- **Language:** Java 21, Spring Boot 
- **Build:** Gradle
- **DB:** PostgreSQL (via Docker) 
- **ORM:** Spring Data JPA (Hibernate) 
- **Connection Pool:** HikariCP (Spring Boot default) 
- **Static Analysis:** Checkstyle, PMD, SpotBugs

**Layered structure**

```text
src/main/java/com/matchmycoffee/
  ├── MatchMyCoffeeApplication.java
  │
  ├── config/                 # Spring & infrastructure config
  ├── controller/             # REST controllers
  ├── service/                # Service interfaces
  ├── repository/             # JPA repositories (interfaces)
  ├── model/
  │   ├── entity/             # JPA entities
  │   ├── enums/              # Enums (e.g. OrderStatus)
  │   └── composite/          # Composite IDs for junction tables
  ├── dto/
  │   ├── request/            # Incoming payloads
  │   └── response/           # API responses
  ├── mapper/                 # MapStruct mappers
  ├── exception/              # Global exception handler, base exception
  ├── security/               # Firebase auth integration
  ├── client/                 # External clients (AI agent)
  └── util/                   # Utility helpers
```

**Domain coverage**

- Taste `taxonomies: taste_categories, tastes`
- Brewing: `brewing_methods`
- Origin: `origins`
- Products & attributes: `products`
- Product relations: `product_origins, product_tastes, product_brewing_methods`
- Orders: `orders, order_items`
- Content: `reviews, blog_posts`

**Build & Run (Gradle)**

```bash
# Build
./gradlew clean build
# Run
./gradlew bootRun
```

**Build Docker image**

```bash
docker build -t mmc-backend .
```

**Lint and Static Analysis**

```bash
./gradlew check
# Reports are generated in build/reports/
```

Typical individual tasks:

```bash
./gradlew checkstyleMain
./gradlew pmdMain
./gradlew spotbugsMain
```


## API Documentation

Planned endpoints (paths can be fine-tuned during implementation):

- `GET /api/products` – list products (filters, paging)

- `GET /api/products/{id}` – product details

- `GET /api/tastes` – list tastes and categories

- `GET /api/brewing-methods` – list brewing methods

- `GET /api/origins` – list coffee origins

Orders:

- `POST /api/orders` – create order

- `GET /api/orders/{orderReference}` – fetch by reference

- `GET /api/orders/email/{customerEmail}` – fetch by email

Reviews & blog:

- `GET /api/products/{id}/reviews`

- `POST /api/products/{id}/reviews`

- `GET /api/blog-posts`

- `GET /api/blog-posts/{id}`
