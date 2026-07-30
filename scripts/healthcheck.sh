#!/bin/bash
# =============================================================================
# PowerSphere – Health Check Script
# =============================================================================
# This script checks the health of all PowerSphere services and outputs
# a summary. Useful for monitoring and troubleshooting.
#
# Usage:
#   ./scripts/healthcheck.sh                    Check all services
#   ./scripts/healthcheck.sh --json             Output as JSON
#   ./scripts/healthcheck.sh --watch            Watch mode (every 5s)
# =============================================================================

set -euo pipefail

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

SERVICES=(
    "Backend:http://localhost:8080/actuator/health"
    "Frontend:http://localhost:80/healthz"
    "Prometheus:http://localhost:9090/-/ready"
    "Grafana:http://localhost:3000/api/health"
    "MailHog:http://localhost:8025"
)

check_service() {
    local name="$1"
    local url="$2"

    if curl -sf --max-time 3 "$url" > /dev/null 2>&1; then
        echo -e "${GREEN}✅${NC} $name - Healthy"
        return 0
    else
        echo -e "${RED}❌${NC} $name - Unhealthy"
        return 1
    fi
}

check_docker() {
    if command -v docker &> /dev/null; then
        if docker ps &> /dev/null; then
            echo -e "${GREEN}✅${NC} Docker - Running"

            # Check expected containers
            local containers=("powersphere-backend" "powersphere-frontend" "powersphere-mysql" "powersphere-redis" "powersphere-prometheus" "powersphere-grafana")
            for container in "${containers[@]}"; do
                if docker ps --format '{{.Names}}' | grep -q "^${container}$"; then
                    local status=$(docker inspect --format='{{.State.Status}}' "$container" 2>/dev/null)
                    if [ "$status" = "running" ]; then
                        echo -e "  ${GREEN}✅${NC} $container ($status)"
                    else
                        echo -e "  ${RED}❌${NC} $container ($status)"
                    fi
                fi
            done
        else
            echo -e "${YELLOW}⚠️${NC} Docker - Not accessible (permissions)"
        fi
    else
        echo -e "${RED}❌${NC} Docker - Not installed"
    fi
}

output_json() {
    echo "{"
    echo '  "timestamp": "'$(date -u +"%Y-%m-%dT%H:%M:%SZ")'",'
    echo '  "services": {'
    local first=true
    for entry in "${SERVICES[@]}"; do
        local name="${entry%%:*}"
        local url="${entry#*:}"
        $first || echo ","
        if curl -sf --max-time 3 "$url" > /dev/null 2>&1; then
            echo -n "    \"$name\": { \"status\": \"healthy\" }"
        else
            echo -n "    \"$name\": { \"status\": \"unhealthy\" }"
        fi
        first=false
    done
    echo ""
    echo '  }'
    echo "}"
}

main() {
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"
    echo -e "${CYAN}  PowerSphere – Health Check                  ${NC}"
    echo -e "${CYAN}  $(date -u '+%Y-%m-%d %H:%M:%S UTC')        ${NC}"
    echo -e "${CYAN}══════════════════════════════════════════════${NC}"
    echo ""

    case "${1:-}" in
        --json)
            output_json
            ;;
        --watch)
            while true; do
                clear
                main
                sleep 5
            done
            ;;
        *)
            check_docker
            echo ""
            for entry in "${SERVICES[@]}"; do
                local name="${entry%%:*}"
                local url="${entry#*:}"
                check_service "$name" "$url"
            done
            echo ""
            echo -e "${CYAN}──────────────────────────────────────────────${NC}"
            echo -e "${CYAN}  Health check complete                       ${NC}"
            echo -e "${CYAN}══════════════════════════════════════════════${NC}"
            ;;
    esac
}

main "$@"
