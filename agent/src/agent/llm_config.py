"""
This module contains configuration settings for the agent module.

Both Groq and Google AI Studio LLMs accept messages in the format:
[("role", "content"), ...]
"""

import logging

from langchain_google_genai import ChatGoogleGenerativeAI
from langchain_groq import ChatGroq

from settings import settings

from .exceptions import LLMError

logger = logging.getLogger(__name__)
_llm = None


def get_llm():
    global _llm
    if _llm is None:
        _llm = initialize_llm()
    return _llm


def initialize_llm():
    logger.info("Initializing LLM with provider: %s", settings.llm.provider)
    if settings.llm.provider == "groq":
        return get_groq_llm()
    elif settings.llm.provider == "ai-studio":
        return get_ai_studio_llm()
    else:
        logger.error("Unsupported LLM provider: %s", settings.llm.provider)
        raise LLMError(f"Unsupported LLM provider: {settings.llm.provider}")


def get_groq_llm() -> ChatGroq:
    try:
        llm = ChatGroq(
            api_key=settings.llm.api_key.get_secret_value(),
            model=settings.llm.model_name,
            temperature=settings.llm.temperature,
            max_tokens=None,
            max_retries=2,
            timeout=None,
            reasoning_format="hidden",
        )
        logger.info("Groq LLM initialized successfully.")
        return llm
    except ValueError as e:
        logger.error("Error initializing Groq LLM: %s", str(e))
        raise LLMError(f"Error initializing Groq LLM: {e}") from e


def get_ai_studio_llm() -> ChatGoogleGenerativeAI:
    try:
        llm = ChatGoogleGenerativeAI(
            api_key=settings.llm.api_key.get_secret_value(),
            model=settings.llm.model_name,
            temperature=settings.llm.temperature,
        )
        logger.info("Google AI Studio LLM initialized successfully.")
        return llm
    except ValueError as e:
        logger.error("Error initializing AI Studio LLM: %s", str(e))
        raise LLMError(f"Error initializing AI Studio LLM: {e}") from e


def test_llm():
    llm_instance = get_llm()
    logger.info("Testing LLM with a sample prompt.")
    try:
        response = llm_instance.invoke([("system", "Hello!")], max_tokens=10)
        logger.info(
            'LLM test response: "%s". LLM is operational.',
            response.content,
        )
    except Exception as e:
        logger.error("LLM test failed: %s", str(e))
        raise LLMError(f"LLM test failed: {e}") from e
