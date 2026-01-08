"""
This module defines the application settings using Pydantic's BaseSettings.
"""

from typing import Optional

from pydantic import Field, FilePath, SecretStr, model_validator
from pydantic_settings import (
    BaseSettings,
    SettingsConfigDict,
    PydanticBaseSettingsSource,
)


class BaseConfig(BaseSettings):
    """
    Base configuration class with common settings.
    Prioritizes .env file values over environment variables.
    """

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    @classmethod
    def settings_customise_sources(
        cls,
        settings_cls: type[BaseSettings],
        init_settings: PydanticBaseSettingsSource,
        env_settings: PydanticBaseSettingsSource,
        dotenv_settings: PydanticBaseSettingsSource,
        file_secret_settings: PydanticBaseSettingsSource,
    ) -> tuple[PydanticBaseSettingsSource, ...]:
        """Prioritize .env file over environment variables."""
        return (
            init_settings,
            dotenv_settings,
            env_settings,
            file_secret_settings,
        )


class LLMConfig(BaseConfig):
    """
    Configuration settings for the Language Model (LLM).
    """

    model_config = SettingsConfigDict(env_prefix="LLM_")

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
            self.validator_system_prompt = (
                self.validator_system_prompt_path.read_text(
                    encoding="utf-8"
                ).strip()
            )

        if not self.system_prompt:
            raise ValueError("system_prompt is required")
        if not self.validator_system_prompt:
            raise ValueError("validator_system_prompt is required")

        return self


class DBConfig(BaseConfig):
    """
    Configuration settings for the Database connection.
    """

    model_config = SettingsConfigDict(env_prefix="DB_")

    host: str
    port: int
    username: str
    password: SecretStr
    database_name: str
    find_coffee_match_query_path: FilePath = None
    find_coffee_match_query: Optional[str] = None

    @model_validator(mode="after")
    def load_queries(self):
        if self.find_coffee_match_query_path:
            self.find_coffee_match_query = (
                self.find_coffee_match_query_path.read_text(
                    encoding="utf-8"
                ).strip()
            )

        if not self.find_coffee_match_query:
            raise ValueError("find_coffee_match_query is required")

        return self


class UvicornConfig(BaseConfig):
    """
    Configuration settings for the Uvicorn server.
    """

    model_config = SettingsConfigDict(env_prefix="UVICORN_")

    host: str
    port: int
    workers: int
    app_reload: bool
    app: str


class FastAPIConfig(BaseConfig):
    """
    Configuration settings for the FastAPI application.
    """

    model_config = SettingsConfigDict(env_prefix="FASTAPI_")

    title: str
    version: str
    description: str
    debug: bool
    allowed_origins: list[str] = [
        "http://localhost:5173",
    ]


class LogConfig(BaseConfig):
    """
    Configuration settings for logging.
    """

    model_config = SettingsConfigDict(env_prefix="LOG_")

    level: str = "INFO"
    format: str = "%(asctime)s [%(levelname)s] %(name)s: %(message)s"


class OtherConfig(BaseConfig):
    """
    Other miscellaneous configuration settings.
    """

    model_config = SettingsConfigDict(env_prefix="OTHER_")

    draw_graph: bool = False


class Settings(BaseConfig):
    """
    Application settings container.
    """

    llm: LLMConfig = Field(default_factory=LLMConfig)
    db: DBConfig = Field(default_factory=DBConfig)
    uvicorn: UvicornConfig = Field(default_factory=UvicornConfig)
    fastapi: FastAPIConfig = Field(default_factory=FastAPIConfig)
    log: LogConfig = Field(default_factory=LogConfig)
    other: OtherConfig = Field(default_factory=OtherConfig)


settings = Settings()
