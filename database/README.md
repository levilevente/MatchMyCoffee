# MatchMyCoffee Database

## Overview

The MatchMyCoffee database module provides a PostgreSQL database backend for the coffee matching application. It stores and manages critical application data including orders, products, coffee preferences, and user taste profiles.

## Technology Stack

- **Database Engine**: PostgreSQL 18 (Alpine Linux)
- **Containerization**: Docker
- **Base Image**: `postgres:18-alpine`
- **Port**: 5432

## Database Structure

The database is automatically initialized with:

- **Schema**: Database structure and tables (`scripts/01-schema.sql`)
- **Seed Data**: Initial data for development and testing (`scripts/02-seed.sql`)

## Quick Start

### Prerequisites

- Docker installed and running
- Available port 5432 on your system

### 1. Build the Database Image

```bash
docker build -t mmc-db .
```

### 2. Start the Database Container

```bash
docker run --rm -d --name mmc-db-instance -p 5432:5432 mmc-db
```

### 3. Verify Database is Running

```bash
docker logs mmc-db-instance
```

## Database Access

### Connect via PostgreSQL Client

```bash
docker exec -it mmc-db-instance psql -U db-user -d match-my-coffee
```

### Container Shell Access

```bash
docker exec -it mmc-db-instance /bin/sh
```

## Database Configuration

| Parameter | Default Value | Description |
|-----------|---------------|-------------|
| `POSTGRES_DB` | `match-my-coffee` | Database name |
| `POSTGRES_USER` | `db-user` | Database username |
| `POSTGRES_PASSWORD` | `Postgres24!` | Database password |
| `POSTGRES_PORT` | `5432` | Database port |

> **Note**: These are default values for development. Override them using environment variables or docker-compose for different environments.

## Management Commands

### Start Database

```bash
docker run --rm -d --name mmc-db-instance -p 5432:5432 mmc-db
```

### Stop Database

```bash
docker stop mmc-db-instance
```

### View Logs

```bash
docker logs mmc-db-instance
```

### Remove Container

```bash
docker rm mmc-db-instance
```

## Environment Variables

You can customize the database configuration by setting environment variables:

```bash
docker run --rm -d \
  --name mmc-db-instance \
  -p 5432:5432 \
  -e POSTGRES_DB=your_db_name \
  -e POSTGRES_USER=your_user \
  -e POSTGRES_PASSWORD=your_password \
  mmc-db
```

## Development Notes

- The database container is configured to remove itself automatically when stopped (`--rm` flag)
- Initial schema and data are loaded automatically on first startup
- Database data persists within the container lifetime but is lost when the container is removed
- For production use, consider mounting a volume for data persistence

## Troubleshooting

### Port Already in Use

If port 5432 is already in use, map to a different port:

```bash
docker run --rm -d --name mmc-db-instance -p 5433:5432 mmc-db
```

### Connection Issues

1. Ensure the container is running: `docker ps`
2. Check container logs: `docker logs mmc-db-instance`
3. Verify port mapping: `docker port mmc-db-instance`

### Reset Database

To start with a fresh database:

```bash
docker stop mmc-db-instance
docker run --rm -d --name mmc-db-instance -p 5432:5432 mmc-db
```
