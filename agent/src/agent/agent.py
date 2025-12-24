"""
This module contains the tool calling agent's configuration.
"""

from langchain.agents import create_agent as create_langchain_agent
from settings import settings
import logging
from .exceptions import WorkflowError
from .llm_config import get_llm
from .state import UserProfile
from .tools import test_tool

logger = logging.getLogger(__name__)

_agent = None
_validator_agent = None


def create_agent():
    global _agent

    tools = [test_tool]

    system_prompt = settings.llm.system_prompt

    try:
        _agent = create_langchain_agent(
            model=get_llm(),
            tools=tools,
            system_prompt=system_prompt,
        )
        logger.info(
            f"Agent created successfully, with model: {settings.llm.model_name}, tools: {[tool.name for tool in tools]}, system_prompt: {'yes' if system_prompt else 'no'}."
        )
    except Exception as e:
        logger.error(f"Failed to create agent: {e}")
        raise WorkflowError(f"Failed to create agent: {e}") from e


def get_agent():
    global _agent
    if _agent is None:
        create_agent()
    return _agent


def create_validator_agent():
    global _validator_agent

    system_prompt = settings.llm.validator_system_prompt

    try:
        _validator_agent = create_langchain_agent(
            model=get_llm(),
            tools=[],
            system_prompt=system_prompt,
            response_format=UserProfile,
        )
        logger.info(
            f"Validator agent created successfully, with model: {settings.llm.model_name}, system_prompt: {'yes' if system_prompt else 'no'}."
        )
    except Exception as e:
        logger.error(f"Failed to create validator agent: {e}")
        raise WorkflowError(f"Failed to create validator agent: {e}") from e


def get_validator_agent():
    global _validator_agent
    if _validator_agent is None:
        create_validator_agent()
    return _validator_agent
