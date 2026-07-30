# =============================================================================
# PowerSphere – Frontend Dockerfile
# Multi-stage build: Node build → Nginx production image
# =============================================================================

# ---- Stage 1: Build with Node.js ----
FROM node:22-alpine AS builder

LABEL stage=builder
LABEL description="PowerSphere Frontend Build Stage"

WORKDIR /build

# Copy package files for dependency caching
COPY package.json package-lock.json ./

# Install dependencies
RUN npm ci --only=production --ignore-scripts && \
    cp -R node_modules /prod_modules && \
    npm ci

# Copy source code
COPY . .

# Build the application
RUN npm run build

# ---- Stage 2: Production with Nginx ----
FROM nginx:1.27-alpine AS runtime

LABEL description="PowerSphere Frontend Runtime"
LABEL version="1.0.0"
LABEL maintainer="PowerSphere Team"

# Install curl for health check
RUN apk add --no-cache curl

# Remove default Nginx config
RUN rm -rf /etc/nginx/conf.d/default.conf

# Copy custom Nginx configuration
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf

# Copy built assets from builder stage
COPY --from=builder /build/dist /usr/share/nginx/html

# Create non-root user and adjust permissions
RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html && \
    chown -R nginx:nginx /var/cache/nginx && \
    touch /var/run/nginx.pid && \
    chown -R nginx:nginx /var/run/nginx.pid

# Switch to non-root user
USER nginx

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:80/healthz || exit 1

# Start Nginx
ENTRYPOINT ["nginx", "-g", "daemon off;"]
