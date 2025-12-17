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

```
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

```bash
# Build the container
docker build -t matchmycoffee-agent .

# Run with docker-compose (from project root)
docker-compose up agent
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
uvicorn src.api:app --reload --port 8001

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
- **AsyncPG** (≥0.31.0): Async PostgreSQL driver for Python
- **Pydantic** (≥2.12.5): Data validation using Python type annotations
- **python-dotenv** (≥1.2.1): Environment variable management

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

- `GET /health` - Health check endpoint
- `POST /recommend` - Get coffee recommendations
- `POST /analyze-taste` - Analyze user taste profile
- `GET /docs` - Interactive API documentation (FastAPI auto-generated)

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
