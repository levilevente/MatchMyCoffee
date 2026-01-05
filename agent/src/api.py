"""
This module contains FastAPI endpoints for the agent module.
"""

import json
import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI, Response
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import StreamingResponse
from langchain_core.messages import HumanMessage
from langgraph.checkpoint.postgres.aio import AsyncPostgresSaver
from pydantic import BaseModel

from agent.graph import create_agent_workflow
from settings import settings

logging.basicConfig(level=settings.log.level, format=settings.log.format)
logger = logging.getLogger(__name__)

_agent_workflow = None


@asynccontextmanager
async def lifespan(_: FastAPI):
    global _agent_workflow
    DB_URI = f"postgresql://{settings.db.username}:{settings.db.password.get_secret_value()}@{settings.db.host}:{settings.db.port}/{settings.db.database_name}?sslmode=disable"

    async with AsyncPostgresSaver.from_conn_string(DB_URI) as checkpointer:
        await checkpointer.setup()
        try:
            _agent_workflow = create_agent_workflow(checkpointer)
            yield
        except Exception as exc:
            logger.error(
                "Failed to initialize agent workflow during startup: %s", exc
            )
            _agent_workflow = None
            raise exc


app = FastAPI(
    debug=settings.fastapi.debug,
    title=settings.fastapi.title,
    description=settings.fastapi.description,
    version=settings.fastapi.version,
    lifespan=lifespan,
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.fastapi.allowed_origins,
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE", "OPTIONS"],
    allow_headers=["*"],
)

STREAMABLE_NODES = {
    "introduction",
    "ask_user_preferences",
    "ask_user_followup",
    "provide_coffee_match",
}


class ChatRequest(BaseModel):
    message: str | None = None
    thread_id: str


def sse_event(event_type: str, thread_id: str, **kwargs) -> str:
    """Format a Server-Sent Event message."""
    data = {"type": event_type, "thread_id": thread_id, **kwargs}
    return f"data: {json.dumps(data)}\n\n"


def is_node_streamable(node_name: str, langgraph_path: list) -> bool:
    """Check if content from this node should be streamed to the client."""
    path_str = str(langgraph_path)

    if node_name in STREAMABLE_NODES or any(
        n in path_str for n in STREAMABLE_NODES
    ):
        return True

    if node_name == "model" and "validate_user_responses" not in path_str:
        return True

    return False


@app.get("/health")
def health_check(response: Response):
    if _agent_workflow is None:
        response.status_code = 503
        return {"status": "unhealthy", "reason": "agent_workflow not loaded"}
    return {"status": "healthy", "agent_workflow": "loaded"}


@app.post("/chat")
async def chat(request: ChatRequest):
    """
    Handle chat messages using Server-Sent Events (SSE) for streaming responses.
    """

    async def event_generator():
        config = {"configurable": {"thread_id": request.thread_id}}
        inputs = (
            {"messages": [HumanMessage(content=request.message)]}
            if request.message
            else {"current_question_index": 0}
        )
        current_node = None

        try:
            async for event in _agent_workflow.astream_events(
                inputs, config=config
            ):
                if event["event"] != "on_chat_model_stream":
                    continue

                metadata = event["metadata"]
                node_name = metadata["langgraph_node"]
                current_node = node_name
                langgraph_path = metadata["langgraph_path"]
                content = event["data"]["chunk"].content

                if not content or not is_node_streamable(
                    node_name, langgraph_path
                ):
                    continue

                yield sse_event("token", request.thread_id, content=content)

            # Check if the workflow reached the recommendation stage
            state = await _agent_workflow.aget_state(config)
            if (
                state.values.get("user_profile")
                and state.values["user_profile"]
                .get("state", "")
                .strip()
                .lower()
                == "ok"
            ):
                yield sse_event("recommendation_complete", request.thread_id)

            yield sse_event("done", request.thread_id)

        except ValueError as e:
            logger.error(
                "Graph execution failed | thread_id=%s | node=%s | "
                "input_message=%s | error=%s",
                request.thread_id,
                current_node or "unknown",
                request.message[:100] if request.message else "None",
                str(e),
                exc_info=True,
            )
            yield sse_event("error", request.thread_id, message=str(e))

    return StreamingResponse(
        event_generator(),
        media_type="text/event-stream",
        headers={"Cache-Control": "no-cache", "Connection": "keep-alive"},
    )


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(
        app="api:app",
        host=settings.uvicorn.host,
        port=settings.uvicorn.port,
        log_level=settings.log.level.lower(),
        reload=settings.uvicorn.app_reload,
    )
