import os
import logging

from langgraph.graph import StateGraph, START, END
from langgraph.checkpoint.memory import MemorySaver

from settings import settings
from .state import WorkflowState
from .nodes import (
    QUESTIONS,
    introduction,
    ask_user_preferences,
    validate_user_responses,
    ask_user_followup,
)

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


def _build_graph() -> StateGraph:
    graph = StateGraph(WorkflowState)

    graph.add_node("introduction", introduction)
    graph.add_node("ask_user_preferences", ask_user_preferences)
    graph.add_node("validate_user_responses", validate_user_responses)
    graph.add_node("ask_user_followup", ask_user_followup)

    graph.add_conditional_edges(START, _route_from_start)
    graph.add_edge("introduction", END)
    graph.add_conditional_edges("ask_user_preferences", _route_after_ask)
    graph.add_edge("validate_user_responses", "ask_user_followup")
    graph.add_edge("ask_user_followup", END)

    return graph


def _save_graph_image(workflow) -> None:
    try:
        os.makedirs("out", exist_ok=True)
        image_data = workflow.get_graph().draw_mermaid_png()
        with open("out/agent_workflow.png", "wb") as f:
            f.write(image_data)
        logger.info("Workflow graph saved as out/agent_workflow.png")
    except Exception as e:
        logger.error(f"Failed to draw workflow graph: {e}")


def create_agent_workflow():
    graph = _build_graph()
    workflow = graph.compile(checkpointer=MemorySaver())

    if settings.other.draw_graph:
        _save_graph_image(workflow)

    return workflow
