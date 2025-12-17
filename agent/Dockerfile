FROM python:3.13-slim

COPY --from=docker.io/astral/uv:latest /uv /uvx /bin/

COPY . /agent

WORKDIR /agent

RUN uv sync

CMD ["bash", "startup.sh"]