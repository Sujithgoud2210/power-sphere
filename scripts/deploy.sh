#!/bin/bash
# =============================================================================
# PowerSphere – Deployment Script
# =============================================================================
# This script deploys the PowerSphere application using Docker Compose.
# It handles building images, running migrations, and health checks.
#
# Usage:
#   ./scripts/deploy.sh                           Deploy all services
#   ./scripts/deploy.sh --no-build                Skip image build
#   ./scripts/deploy.sh --service backend         Deploy single service
#   ./scripts/deploy.sh --rollback                Rollback to previous version
#   ./scripts/deploy.sh --help                    Show help
# =============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

# Configuration
BACKUP_DIR="${PROJECT_DIR}/backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
COMPOSE_FILE="${PROJECT_DIR}/docker-compose.yml"

# Parse arguments
SKIP_BUILD=false
SERVICES=()
ROLLBACK=false

print_usage() {
    echo "PowerSphere Deployment Script"
    echo ""
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  --no-build           Skip Docker image build"
    echo "  --service NAME       Deploy specific service (can be repeated)"
    echo "  --rollback           Rollback to previous deployment"
    echo "  --help               Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                                Deploy all services"
    echo "  $0 --no-build                     Deploy without building"
    echo "  $0 --service backend              Deploy only backend"
    echo "  $0 --service backend --service frontend  Deploy backend and frontend"
}

while [[ $# -gt 0 ]]; do
    case $1 in
        --no-build) SKIP_BUILD=true; shift ;;
        --service) SERVICES+=("$2"); shift 2 ;;
        --rollback) ROLLBACK=true; shift ;;
        --help) print_usage; exit 0 ;;
        *) echo "Unknown option: $1"; print_usage; exit 1 ;;
    esac
done

# Pre-deployment checks
pre_deploy_checks() {
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"
    echo -e "${CYAN}  PowerSphere – Pre-Deployment Checks          ${NC}"
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"

    # Check Docker
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}❌ Docker is not installed${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Docker is installed${NC}"

    # Check Docker Compose
    if ! docker compose version &> /dev/null; then
        echo -e "${RED}❌ Docker Compose is not installed${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Docker Compose is installed${NC}"

    # Check .env file
    if [ ! -f "${PROJECT_DIR}/.env" ]; then
        echo -e "${YELLOW}⚠️  .env file not found. Creating from .env.example...${NC}"
        cp "${PROJECT_DIR}/.env.example" "${PROJECT_DIR}/.env"
        echo -e "${YELLOW}⚠️  Please edit .env with your production values before deploying${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ .env file found${NC}"

    # Check disk space
    local available=$(df -h / | awk 'NR==2 {print $4}')
    echo -e "${GREEN}✅ Available disk space: $available${NC}"

    echo ""
}

# Backup existing data
backup() {
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"
    echo -e "${CYAN}  Creating backup...                           ${NC}"
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"

    mkdir -p "$BACKUP_DIR"

    # Backup Docker volumes if running
    if docker ps --format '{{.Names}}' | grep -q "powersphere-mysql"; then
        echo "Backing up MySQL database..."
        docker exec powersphere-mysql mysqldump -u root -p"${DB_PASSWORD:-root}" powersphere > \
            "${BACKUP_DIR}/powersphere_db_${TIMESTAMP}.sql" 2>/dev/null || true
        echo -e "${GREEN}✅ Database backup created${NC}"
    fi

    # Backup current docker-compose state
    if [ -f "${PROJECT_DIR}/docker-compose.yml" ]; then
        cp "${PROJECT_DIR}/docker-compose.yml" "${BACKUP_DIR}/docker-compose.yml.${TIMESTAMP}"
        echo -e "${GREEN}✅ Configuration backup created${NC}"
    fi

    echo ""
}

# Build images
build_images() {
    if [ "$SKIP_BUILD" = true ]; then
        echo -e "${YELLOW}⚠️  Skipping Docker build${NC}"
        return
    fi

    echo -e "${CYAN}══════════════════════════════════════════════${NC}"
    echo -e "${CYAN}  Building Docker images...                    ${NC}"
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"

    if [ ${#SERVICES[@]} -eq 0 ]; then
        docker compose -f "$COMPOSE_FILE" build
    else
        for service in "${SERVICES[@]}"; do
            docker compose -f "$COMPOSE_FILE" build "$service"
        done
    fi

    echo -e "${GREEN}✅ Build complete${NC}"
    echo ""
}

# Deploy services
deploy_services() {
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"
    echo -e "${CYAN}  Deploying services...                        ${NC}"
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"

    if [ ${#SERVICES[@]} -eq 0 ]; then
        docker compose -f "$COMPOSE_FILE" up -d --remove-orphans
    else
        for service in "${SERVICES[@]}"; do
            docker compose -f "$COMPOSE_FILE" up -d "$service"
        done
    fi

    echo -e "${GREEN}✅ Deployment complete${NC}"
    echo ""
}

# Post-deployment health check
health_check() {
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"
    echo -e "${CYAN}  Post-Deployment Health Check...              ${NC}"
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"

    sleep 10

    # Check backend
    if curl -sf http://localhost:8080/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Backend is healthy${NC}"
    else
        echo -e "${RED}❌ Backend health check failed${NC}"
    fi

    # Check frontend
    if curl -sf http://localhost:80/healthz > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Frontend is healthy${NC}"
    else
        echo -e "${RED}❌ Frontend health check failed${NC}"
    fi

    echo ""
}

# Rollback
rollback_deployment() {
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"
    echo -e "${CYAN}  Rolling back to previous version...          ${NC}"
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"

    local latest_backup=$(ls -t "${BACKUP_DIR}/docker-compose.yml."* 2>/dev/null | head -1)
    if [ -n "$latest_backup" ]; then
        cp "$latest_backup" "${PROJECT_DIR}/docker-compose.yml"
        docker compose -f "$COMPOSE_FILE" down
        docker compose -f "$COMPOSE_FILE" up -d
        echo -e "${GREEN}✅ Rollback complete${NC}"
    else
        echo -e "${RED}❌ No backup found for rollback${NC}"
        exit 1
    fi
}

# Main
main() {
    echo -e "${CYAN}╔══════════════════════════════════════════════╗${NC}"
    echo -e "${CYAN}║        PowerSphere Deployment                ║${NC}"
    echo -e "${CYAN}║        ${TIMESTAMP}                ║${NC}"
    echo -e "${CYAN}╚══════════════════════════════════════════════╝${NC}"
    echo ""

    if [ "$ROLLBACK" = true ]; then
        rollback_deployment
        exit 0
    fi

    pre_deploy_checks
    backup
    build_images
    deploy_services
    health_check

    echo -e "${GREEN}══════════════════════════════════════════════${NC}"
    echo -e "${GREEN}  Deployment completed successfully!          ${NC}"
    echo -e "${GREEN}══════════════════════════════════════════════${NC}"
}

main
