FROM python:3.13-slim

COPY --from=docker.io/astral/uv:0.9.18 /uv /uvx /bin/

COPY . /agent

WORKDIR /agent

RUN uv sync

CMD ["bash", "startup.sh"]