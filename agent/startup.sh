#!/bin/bash
# Startup script for MatchMyCoffee Agent
# This script performs pre-flight checks before starting the application

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "========================================"
echo "  MatchMyCoffee Agent - Startup Script"
echo "========================================"
echo ""

# Function to print status messages
print_status() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

# Track if any checks fail
CHECKS_PASSED=true

echo "Running pre-flight checks..."
echo ""

# Check 1: Verify .env file exists
echo "1. Checking .env file..."
if [ ! -f ".env" ]; then
    print_error ".env file not found!"
    CHECKS_PASSED=false
else
    # Check if .env has content (not empty)
    if [ ! -s ".env" ]; then
        print_error ".env file is empty!"
        CHECKS_PASSED=false
    else
        # Check for required environment variables
        REQUIRED_VARS=("LLM_PROVIDER" "LLM_API_KEY" "LLM_MODEL_NAME" "DB_HOST" "DB_PORT" "DB_USERNAME" "DB_PASSWORD" "DB_DATABASE_NAME" "DB_FIND_COFFEE_MATCH_QUERY_PATH")
        MISSING_VARS=()
        
        for var in "${REQUIRED_VARS[@]}"; do
            if ! grep -q "^${var}=" .env; then
                MISSING_VARS+=("$var")
            fi
        done
        
        if [ ${#MISSING_VARS[@]} -gt 0 ]; then
            print_error "Missing required environment variables in .env:"
            for var in "${MISSING_VARS[@]}"; do
                echo "         - $var"
            done
            CHECKS_PASSED=false
        else
            print_status ".env file present with required variables"
        fi
    fi
fi

# Check 2: Verify static files exist
echo "2. Checking static files..."
STATIC_FILES=("static/system_prompt.txt" "static/validator_system_prompt.txt" "static/find_coffee_match_query.sql")
MISSING_FILES=()

for file in "${STATIC_FILES[@]}"; do
    if [ ! -f "$file" ]; then
        MISSING_FILES+=("$file")
    fi
done

if [ ${#MISSING_FILES[@]} -gt 0 ]; then
    print_error "Missing required static files:"
    for file in "${MISSING_FILES[@]}"; do
        echo "         - $file"
    done
    CHECKS_PASSED=false
else
    print_status "All static files present"
fi

# Check 3: Verify static files are not empty
echo "3. Checking static file contents..."
EMPTY_FILES=()

for file in "${STATIC_FILES[@]}"; do
    if [ -f "$file" ] && [ ! -s "$file" ]; then
        EMPTY_FILES+=("$file")
    fi
done

if [ ${#EMPTY_FILES[@]} -gt 0 ]; then
    print_error "Static files are empty:"
    for file in "${EMPTY_FILES[@]}"; do
        echo "         - $file"
    done
    CHECKS_PASSED=false
else
    print_status "All static files have content"
fi

# Check 4: Verify Python and uv are available
echo "4. Checking Python environment..."
if ! command -v uv &> /dev/null; then
    print_error "uv is not installed or not in PATH"
    CHECKS_PASSED=false
else
    print_status "uv is available"
fi

if ! command -v python &> /dev/null && ! command -v python3 &> /dev/null; then
    print_error "Python is not installed or not in PATH"
    CHECKS_PASSED=false
else
    print_status "Python is available"
fi

# Check 5: Verify src/api.py exists
echo "5. Checking application entry point..."
if [ ! -f "src/api.py" ]; then
    print_error "src/api.py not found!"
    CHECKS_PASSED=false
else
    print_status "Application entry point found"
fi

echo ""
echo "========================================"

# Final check result
if [ "$CHECKS_PASSED" = false ]; then
    print_error "Pre-flight checks failed! Please fix the issues above."
    exit 1
fi

print_status "All pre-flight checks passed!"
echo ""
echo "Starting MatchMyCoffee Agent..."
echo "========================================"
echo ""

# Start the application
exec uv run src/api.py
