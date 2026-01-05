"""
This module contains tool definitions for the agent.
"""

from langchain_core.tools import tool

from data.database import get_brewing_methods as db_get_brewing_methods
from data.database import get_taste_categories as db_get_taste_categories


@tool()
def get_taste_categories():
    """
    Returns currently available taste categories and their tastes for coffee.
    No parameters required. Returns a dictionary where keys are category names
    and values are lists of taste names within that category.
    Example: {"Fruity": ["Berry", "Citrus"], "Nutty": ["Hazelnut", "Almond"]}
    """
    return db_get_taste_categories()


@tool()
def get_brewing_methods():
    """
    Returns a list of currently available brewing method names for coffee.
    No parameters required. Returns a simple list of strings.
    Example: ["Espresso", "French Press", "Pour Over"]
    """
    return db_get_brewing_methods()
