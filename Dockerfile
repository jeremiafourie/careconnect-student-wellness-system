# Multi-stage build for CareConnect application
FROM maven:3.9.10-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the application
RUN mvn package -DskipTests

# Final stage
FROM jetty:12.0.23-jdk17-eclipse-temurin

# Add EE10 deployment and JSP modules
RUN java -jar "$JETTY_HOME/start.jar" --add-modules=ee10-deploy,ee10-jsp

# Copy the built WAR file
COPY --from=builder /app/web/target/web-1.0-SNAPSHOT.war /var/lib/jetty/webapps/ROOT.war

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1