# ☕ MatchMyCoffee - AI Agent Module

![Python](https://img.shields.io/badge/Python-3.13+-3776AB?style=flat&logo=python)
![LangGraph](https://img.shields.io/badge/LangGraph-1.0.5+-FF6B6B?style=flat&logo=python)
![FastAPI](https://img.shields.io/badge/FastAPI-0.124.4+-009688?style=flat&logo=fastapi)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-AsyncPG-336791?style=flat&logo=postgresql)

The **Agent Module** is the AI-powered core of MatchMyCoffee, providing intelligent coffee recommendations through LangGraph workflows and FastAPI endpoints. This module serves as a separate Python package within the MatchMyCoffee ecosystem, designed for scalability and Docker Compose compatibility.

## 🎯 Purpose

This module serves as an **API for recommending coffee** by:

- Processing user taste profiles and preferences
- Leveraging LangGraph for complex reasoning workflows
- Integrating with the PostgreSQL database for coffee data
- Communicating with the Spring Boot backend for user management
- Providing RESTful endpoints for the frontend React application

## 🏗️ Architecture

The agent module follows a **clean architecture** pattern with clear separation of concerns:

```sh
src/
├── api.py              # FastAPI routes and endpoint definitions
├── agent/              # LangGraph workflow components
│   ├── __init__.py
│   ├── graph.py        # LangGraph state machines and workflow logic
│   ├── nodes.py        # Individual processing nodes for the workflow
│   └── tools.py        # Tool definitions for agent capabilities
└── data/               # Data access and external service integration
    ├── __init__.py
    ├── backend.py      # Spring Boot backend communication
    ├── config.py       # Configuration management
    └── database.py     # PostgreSQL database operations
```

### 🔄 Component Responsibilities

- **API Layer (`api.py`)**: FastAPI endpoints that expose agent functionality
- **Agent Layer (`agent/`)**: LangGraph-powered reasoning and workflow orchestration
- **Data Layer (`data/`)**: External service integrations and data persistence

## 🚀 Getting Started

### Prerequisites

- **Python**: 3.13+
- **uv**: Modern Python package manager
- **PostgreSQL**: Database connection (local or containerized)
- **Spring Boot Backend**: Running backend service (optional for development)

### Installation

1. **Install uv** (if not already installed):

   ```bash
   curl -LsSf https://astral.sh/uv/install.sh | sh
   ```

2. **Create and activate virtual environment**:

   ```bash
   uv venv && source .venv/bin/activate
   ```

3. **Install dependencies**:

   ```bash
   uv sync
   ```

4. **Configure environment** (copy and edit `.env.example`):

   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

5. **Run the agent**:

   ```bash
   # Development mode
   uvicorn src.api:app --reload --host 0.0.0.0 --port 8001
   ```

## 🐳 Docker Support

The module is **Docker Compose ready** for seamless integration:

### Build & Run Commands

```bash
# Build the Docker image
docker build -t mmc-agent .

# Run container with auto-remove (stops and removes when done)
docker run --rm -p 8000:8000 --name mmc-agent-instance mmc-agent

# Run container detached with auto-remove
docker run -d --rm -p 8000:8000 --name mmc-agent-instance mmc-agent

# Run with environment file
docker run --rm -p 8000:8000 --env-file .env --name mmc-agent-instance mmc-agent

# Stop the running container (auto-removed due to --rm flag)
docker stop mmc-agent-instance
```

### Docker Compose Integration

```bash
# Run with docker-compose (from project root)
docker-compose up agent

# Run in detached mode
docker-compose up -d agent
```

### Health Check

The Docker container includes a built-in health check that monitors the `/health` endpoint every 30 seconds. You can check container health status with:

```bash
docker inspect --format='{{.State.Health.Status}}' mmc-agent-instance
```

## 🧰 Development Workflow

### Code Quality with Pylint

The project uses **Google Python Style Guide** enforced by Pylint:

```bash
# Run linting
pylint --rcfile .pylintrc .

# Run linting on specific files
pylint src/api.py src/agent/
```

**Pylint Integration**:

- ✅ **Pre-configured**: Uses Google's official `.pylintrc` configuration
- ✅ **IDE Integration**: Works with VS Code Python extension
- ✅ **CI/CD Ready**: Can be integrated into GitHub Actions
- ✅ **Team Consistency**: Enforces consistent code style across developers

### Development Commands

```bash
# Install development dependencies
uv sync --group dev

# Run code quality checks
pylint --rcfile .pylintrc .

# Start development server
uvicorn src.api:app --reload --port 8000

# Run tests (when implemented)
pytest tests/
```

### Recommended VS Code Extensions

- **Python**: Microsoft's Python extension
- **Pylint**: Real-time linting integration
- **Python Docstring Generator**: For consistent documentation

## 📦 Dependencies

### Production Dependencies

- **FastAPI** (≥0.124.4): Modern web framework for building APIs
- **LangGraph** (≥1.0.5): Orchestration framework for LLM workflows
- **LangChain Groq** (≥1.1.1): Groq LLM integration
- **LangChain Google GenAI** (≥1.2.0): Google AI integration
- **Psycopg** (≥3.3.2): PostgreSQL driver for Python
- **Pydantic** (≥2.12.5): Data validation using Python type annotations
- **Pydantic Settings** (≥2.12.0): Settings management with Pydantic
- **Uvicorn** (≥0.38.0): ASGI server for FastAPI

### Development Dependencies

- **Pylint** (≥4.0.4): Code analysis and style checking

## 🔧 Configuration

The module uses environment variables for configuration. See `.env.example` for required settings:

- Database connection strings
- API endpoints for backend services
- AI model configuration
- Logging levels

## 🚦 API Endpoints

When running, the agent exposes the following endpoints:

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/health` | GET | Health check endpoint - returns agent workflow status |
| `/chat` | POST | SSE streaming chat endpoint for coffee recommendations |
| `/docs` | GET | Interactive API documentation (FastAPI auto-generated) |

### Chat Endpoint

The `/chat` endpoint uses Server-Sent Events (SSE) for real-time streaming responses:

```bash
curl -X POST http://localhost:8000/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello", "thread_id": "user-123"}'
```

### Health Check

```bash
curl http://localhost:8000/health
# Response: {"status": "healthy", "agent_workflow": "loaded"}
```

## 🤝 Integration Points

### With Frontend (React)

```typescript
// Example API call from React frontend
const recommendations = await fetch('/api/agent/recommend', {
  method: 'POST',
  body: JSON.stringify({ userId, preferences })
});
```

### With Backend (Spring Boot)

```python
# Example backend integration
user_data = await backend.get_user_profile(user_id)
recommendations = await agent.recommend_coffee(user_data)
```

### With Database (PostgreSQL)

```python
# Example database query
coffees = await db.fetch_coffees_by_taste_profile(taste_vector)
```

## 🔮 Future Extensions

The architecture is designed to easily support:

- Additional LangGraph workflows
- New AI model integrations
- Extended API endpoints
- Advanced recommendation algorithms
- Real-time chat capabilities
- Integration with external coffee databases

## 📝 Contributing

1. Follow the Google Python Style Guide
2. Run `pylint` before committing
3. Add docstrings to all functions and classes
4. Update tests when adding new functionality
5. Ensure Docker compatibility is maintained

---

*This module is part of the **MatchMyCoffee** ecosystem. See the main repository README for full system documentation.*
