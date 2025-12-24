from typing import TypedDict, Annotated

from langchain_core.messages import BaseMessage
from langgraph.graph.message import add_messages


class UserProfile(TypedDict):
    state: str | None
    brewing_method: str | None
    min_roast_level: int
    max_roast_level: int
    min_acidity: int
    max_acidity: int
    target_flavor_keywords: list[str]
    prefer_single_origin: bool | None


class WorkflowState(TypedDict):
    messages: Annotated[list[BaseMessage], add_messages]
    current_question_index: int
    user_profile: UserProfile
