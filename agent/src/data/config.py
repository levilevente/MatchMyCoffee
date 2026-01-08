"""
Data module configuration.
"""

from src.settings import DBConfig, settings


def get_db_config() -> DBConfig:
    """Get the database configuration."""
    return settings.db
