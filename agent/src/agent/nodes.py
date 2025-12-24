import logging

from langchain_core.messages import SystemMessage

from .state import WorkflowState
from .agent import get_agent, get_validator_agent

logger = logging.getLogger(__name__)

QUESTIONS = [
    "How do you usually prepare your coffee at home? (e.g., Espresso machine, French Press, Pour-over/V60, Moka Pot, or just a standard drip machine?)",
    "How do you drink it? Pure black, with a splash of milk/cream, or heavily mixed (like lattes and cappuccinos)?",
    "Imagine your perfect morning cup. Which set of flavors sounds most appealing to you right now? Options: A) Bright, fruity, and tea-like (Berries, Citrus, Floral); B) Sweet, smooth, and comforting (Chocolate, Caramel, Nuts); C) Deep, intense, and spicy (Earth, Tobacco, Dark Cocoa)",
    "How adventurous are you feeling? Do you want a classic 'coffee that tastes like coffee', or are you looking for something funky and experimental?",
]

INTRO_PROMPT = """
Start by introducing yourself and explain that you'll be asking a few questions 
to help find their perfect coffee match. Keep it friendly, engaging and short.
Then, naturally transition to asking this first question: '{question}'
"""

QUESTION_PROMPT = "Ask the user this question: '{question}'. Introduce it naturally based on the conversation so far."

FOLLOWUP_PROMPT = """
The user's previous answer didn't quite answer the question. 
The issue with the user's responses: {issue}. 
Politely acknowledge their input but ask the question again or clarify what you need.
"""


def _invoke_agent_with_system_prompt(messages: list, prompt: str) -> dict:
    agent = get_agent()
    response = agent.invoke({"messages": [SystemMessage(content=prompt)] + messages})
    return response["messages"][-1]


def introduction(state: WorkflowState) -> dict:
    prompt = INTRO_PROMPT.format(question=QUESTIONS[0])
    response = _invoke_agent_with_system_prompt([], prompt)
    return {"messages": [response], "current_question_index": 1}


def ask_user_preferences(state: WorkflowState) -> dict:
    index = state["current_question_index"]

    if index < len(QUESTIONS):
        prompt = QUESTION_PROMPT.format(question=QUESTIONS[index])
        response = _invoke_agent_with_system_prompt(state["messages"], prompt)
        return {"messages": [response], "current_question_index": index + 1}

    return {"current_question_index": index + 1}


def validate_user_responses(state: WorkflowState) -> dict:
    logger.debug("Validating user responses with validator agent.")
    agent = get_validator_agent()
    response = agent.invoke({"messages": state["messages"]})
    logger.debug(f"Validator agent response: {response}")
    return {
        "messages": [response["messages"][-1]],
        "user_profile": response["structured_response"],
    }


def ask_user_followup(state: WorkflowState) -> dict:
    profile_state = state["user_profile"]["state"]

    if profile_state.strip().lower() == "ok":
        return {}

    prompt = FOLLOWUP_PROMPT.format(issue=profile_state)
    response = _invoke_agent_with_system_prompt(state["messages"], prompt)
    return {"messages": [response]}
