from typing import TypedDict, Annotated

from langchain_core.messages import BaseMessage
from langgraph.graph.message import add_messages


class UserProfile(TypedDict):
    state: str | None
    brewing_method: str | None
    min_roast_level: int | None
    max_roast_level: int | None
    min_acidity: int | None
    max_acidity: int | None
    target_flavor_keywords: list[str] | None
    prefer_single_origin: bool | None


class WorkflowState(TypedDict):
    messages: Annotated[list[BaseMessage], add_messages]
    current_question_index: int
    user_profile: UserProfile
