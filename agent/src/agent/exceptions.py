"""
This module defines custom exceptions for the agent.
"""


class LLMError(Exception):
    """Base exception for LLM-related errors."""

    def __init__(self, message: str):
        super().__init__(message)
        self.message = message


class WorkflowError(Exception):
    """Base exception for Workflow-related errors."""

    def __init__(self, message: str):
        super().__init__(message)
        self.message = message
