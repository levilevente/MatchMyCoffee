FROM postgres:18-alpine

LABEL description "PostgreSQL database for MatchMyCoffee project with pre-loaded schema and data"

COPY ./scripts/ /docker-entrypoint-initdb.d/

# Default values; CLI and docker-compose values will override these
ENV POSTGRES_DB="match-my-coffee"
ENV POSTGRES_USER="db-user"
ENV POSTGRES_PASSWORD="Postgres24!"

HEALTHCHECK --interval=2s --timeout=1s --start-period=3s --retries=5 CMD [ "pg_isready", "-U", "db-user", "-d", "match-my-coffee" ]

EXPOSE 5432