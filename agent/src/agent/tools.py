"""
This module contains tool definitions for the agent.
"""

from langchain_core.tools import tool


@tool()
def test_tool():
    """
    A simple test tool to verify tool integration.
    """
    return "This tool is available and functioning properly."
