"""
This module defines the application settings using Pydantic's BaseSettings.
"""

from typing import Optional
from pydantic import Field, SecretStr, FilePath, model_validator
from pydantic_settings import BaseSettings, SettingsConfigDict


class LLMConfig(BaseSettings):
    model_config = SettingsConfigDict(
        env_prefix="LLM_",
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    provider: str
    api_key: SecretStr
    model_name: str
    temperature: float = 0.1
    system_prompt_path: Optional[FilePath] = None
    system_prompt: Optional[str] = None
    validator_system_prompt_path: Optional[FilePath] = None
    validator_system_prompt: Optional[str] = None

    @model_validator(mode="after")
    def load_system_prompts(self):
        if self.system_prompt_path:
            self.system_prompt = self.system_prompt_path.read_text(
                encoding="utf-8"
            ).strip()
        if self.validator_system_prompt_path:
            self.validator_system_prompt = self.validator_system_prompt_path.read_text(
                encoding="utf-8"
            ).strip()

        if not self.system_prompt:
            raise ValueError("system_prompt is required")
        if not self.validator_system_prompt:
            raise ValueError("validator_system_prompt is required")

        return self


class DBConfig(BaseSettings):
    model_config = SettingsConfigDict(
        env_prefix="DB_",
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    host: str
    port: int
    username: str
    password: SecretStr
    database_name: str


class UvicornConfig(BaseSettings):
    model_config = SettingsConfigDict(
        env_prefix="UVICORN_",
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    host: str
    port: int
    workers: int
    app_reload: bool
    app: str


class FastAPIConfig(BaseSettings):
    model_config = SettingsConfigDict(
        env_prefix="FASTAPI_",
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    title: str
    version: str
    description: str
    debug: bool
    allowed_origins: list[str] = [
        "http://localhost:5173",
    ]


class LogConfig(BaseSettings):
    model_config = SettingsConfigDict(
        env_prefix="LOG_",
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    level: str = "INFO"
    format: str = "%(asctime)s [%(levelname)s] %(name)s: %(message)s"


class OtherConfig(BaseSettings):
    model_config = SettingsConfigDict(
        env_prefix="OTHER_",
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    draw_graph: bool = False


class Settings(BaseSettings):
    """Application settings container."""

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    llm: LLMConfig = Field(default_factory=LLMConfig)
    db: DBConfig = Field(default_factory=DBConfig)
    uvicorn: UvicornConfig = Field(default_factory=UvicornConfig)
    fastapi: FastAPIConfig = Field(default_factory=FastAPIConfig)
    log: LogConfig = Field(default_factory=LogConfig)
    other: OtherConfig = Field(default_factory=OtherConfig)


settings = Settings()
