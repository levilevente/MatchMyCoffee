"""
Defines the agent workflow graph for MatchMyCoffee.
"""

import logging
import os

from langgraph.graph import END, START, StateGraph

from settings import settings

from .nodes import (
    QUESTIONS,
    ask_user_followup,
    ask_user_preferences,
    introduction,
    provide_coffee_match,
    validate_user_responses,
)
from .state import WorkflowState

logger = logging.getLogger(__name__)


def _route_from_start(state: WorkflowState) -> str:
    index = state.get("current_question_index", 0)
    if index == 0:
        return "introduction"
    if index <= len(QUESTIONS):
        return "ask_user_preferences"
    return "validate_user_responses"


def _route_after_ask(state: WorkflowState) -> str:
    if state["current_question_index"] <= len(QUESTIONS):
        return END
    return "validate_user_responses"


def _route_after_validation(state: WorkflowState) -> str:
    """Route based on whether user profile needs followup."""
    profile_state = state.get("user_profile", {}).get("state", "")
    if profile_state.strip().lower() == "ok":
        return "provide_coffee_match"
    return "ask_user_followup"


def _build_graph() -> StateGraph:
    graph = StateGraph(WorkflowState)

    graph.add_node("introduction", introduction)
    graph.add_node("ask_user_preferences", ask_user_preferences)
    graph.add_node("validate_user_responses", validate_user_responses)
    graph.add_node("ask_user_followup", ask_user_followup)
    graph.add_node("provide_coffee_match", provide_coffee_match)

    graph.add_conditional_edges(START, _route_from_start)
    graph.add_edge("introduction", END)
    graph.add_conditional_edges("ask_user_preferences", _route_after_ask)
    graph.add_conditional_edges(
        "validate_user_responses", _route_after_validation
    )
    graph.add_edge("ask_user_followup", END)
    graph.add_edge("provide_coffee_match", END)

    return graph


def _save_graph_image(workflow) -> None:
    try:
        os.makedirs("out", exist_ok=True)
        image_data = workflow.get_graph().draw_mermaid_png()
        with open("out/agent_workflow.png", "wb") as f:
            f.write(image_data)
        logger.info("Workflow graph saved as out/agent_workflow.png")
    except FileNotFoundError as e:
        logger.error("Failed to draw workflow graph: %s", str(e))


def create_agent_workflow(checkpointer=None):
    graph = _build_graph()

    workflow = graph.compile(checkpointer=checkpointer)

    if settings.other.draw_graph:
        _save_graph_image(workflow)

    return workflow
