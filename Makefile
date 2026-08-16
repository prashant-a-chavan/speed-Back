.PHONY: help format check build

help:
	@echo "Available commands:"
	@echo "  make format   - Apply code formatting to all Java files."
	@echo "  make check    - Check if all Java files are correctly formatted."
	@echo "  make build    - Build the Spring Boot application (includes format check)."

format:
	@echo "Applying code formatting..."
	./mvnw spotless:apply

check:
	@echo "Checking code formatting..."
	./mvnw spotless:check

build:
	@echo "Building the application..."
	./mvnw clean install