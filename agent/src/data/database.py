"""
This module manages the connection with the PostgreSQL database.
"""

import logging
from contextlib import contextmanager

from psycopg_pool import ConnectionPool

from settings import settings

logger = logging.getLogger(__name__)

_pool = None


def init_db():
    global _pool

    if _pool is None:
        _pool = ConnectionPool(
            min_size=1,
            max_size=10,
            kwargs={
                "dbname": settings.db.database_name,
                "user": settings.db.username,
                "password": settings.db.password.get_secret_value(),
                "host": settings.db.host,
                "port": settings.db.port,
            },
        )
    return _pool


@contextmanager
def get_db_connection():
    global _pool

    if _pool is None:
        init_db()

    with _pool.connection() as conn:
        yield conn


def close_db():
    global _pool

    if _pool is not None:
        _pool.close()
        _pool = None


def get_taste_categories() -> dict[str, list[str]]:
    query = """
    SELECT tc.name AS category, t.name AS taste
    FROM taste_categories tc
    JOIN tastes t ON tc.id = t.category_id
    ORDER BY tc.name, t.name;
    """
    with get_db_connection() as conn:
        with conn.cursor() as cur:
            cur.execute(query)
            results = cur.fetchall()

    taste_dict = {}
    for category, taste in results:
        if category not in taste_dict:
            taste_dict[category] = []
        taste_dict[category].append(taste)
    return taste_dict


def get_brewing_methods() -> list:
    query = "SELECT name FROM brewing_methods ORDER BY name;"
    with get_db_connection() as conn:
        with conn.cursor() as cur:
            cur.execute(query)
            results = cur.fetchall()

    return [row[0] for row in results]


def find_coffee_match(user_profile: dict) -> list[dict]:
    query = settings.db.find_coffee_match_query

    with get_db_connection() as conn:
        with conn.cursor() as cur:
            cur.execute(
                query,
                (
                    user_profile["brewing_method"],
                    user_profile["min_roast_level"],
                    user_profile["max_roast_level"],
                    user_profile["min_acidity"],
                    user_profile["max_acidity"],
                    user_profile["target_flavor_keywords"],
                    (
                        user_profile["prefer_single_origin"]
                        if user_profile["prefer_single_origin"] is not None
                        else False
                    ),
                ),
            )
            results = cur.fetchall()

    if results:
        return [
            {
                "id": row[0],
                "name": row[1],
                "description": row[2],
                "price": float(row[3]),
                "roast_level": row[4],
                "acidity_score": row[5],
                "brewing_method": row[6],
                "brewing_method_is_optimal": row[7],
                "tastes": row[8],
                "is_blend": row[9],
                "blend": row[10],
            }
            for row in results
        ]
    else:
        return []
