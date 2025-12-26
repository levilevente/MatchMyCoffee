FROM python:3.13-slim

COPY --from=docker.io/astral/uv:0.9.18 /uv /uvx /bin/

RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

COPY . /agent

WORKDIR /agent

RUN uv sync

RUN chmod +x startup.sh

HEALTHCHECK --interval=30s --timeout=10s --start-period=15s --retries=3 \
    CMD curl -f http://localhost:8000/health || exit 1

# Warning: .dockerignore does not exclude .env so do not push image on remote registry if it contains sensitive data

CMD ["bash", "startup.sh"]