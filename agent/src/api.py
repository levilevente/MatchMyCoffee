"""
This module contains FastAPI endpoints for the agent module.
"""

from fastapi import FastAPI, Response
from fastapi.responses import StreamingResponse
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from langchain_core.messages import HumanMessage
from settings import settings
from agent.graph import create_agent_workflow
from utils.ThinkBlockFilter import ThinkBlockFilter
import logging
import json

logging.basicConfig(level=settings.log.level, format=settings.log.format)
logger = logging.getLogger(__name__)

app = FastAPI(
    debug=settings.fastapi.debug,
    title=settings.fastapi.title,
    description=settings.fastapi.description,
    version=settings.fastapi.version,
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.fastapi.allowed_origins,
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE", "OPTIONS"],
    allow_headers=["*"],
)

_agent_workflow = create_agent_workflow()

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

    if node_name in STREAMABLE_NODES or any(n in path_str for n in STREAMABLE_NODES):
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
        think_filter = ThinkBlockFilter()

        try:
            async for event in _agent_workflow.astream_events(inputs, config=config):
                if event["event"] != "on_chat_model_stream":
                    continue

                metadata = event["metadata"]
                node_name = metadata["langgraph_node"]
                langgraph_path = metadata["langgraph_path"]
                content = event["data"]["chunk"].content

                if not content or not is_node_streamable(node_name, langgraph_path):
                    continue

                chunks, send_thinking_status = think_filter.process(content, node_name)

                for chunk in chunks:
                    yield sse_event("token", request.thread_id, content=chunk)

                if send_thinking_status:
                    yield sse_event("status", request.thread_id, status="thinking")

            # Check if the workflow reached the recommendation stage
            state = await _agent_workflow.aget_state(config)
            if (
                state.values.get("user_profile")
                and state.values["user_profile"].get("state", "").strip().lower()
                == "ok"
            ):
                yield sse_event("recommendation_complete", request.thread_id)

            yield sse_event("done", request.thread_id)

        except Exception as e:
            logger.error(f"Error during graph execution: {e}")
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
