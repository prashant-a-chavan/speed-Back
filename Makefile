.PHONY: help format format-check build tests start setup

help:
	@echo "Available commands:"
	@echo "  make format        - Apply code formatting to all Java files."
	@echo "  make format-check  - Check if all Java files are correctly formatted."
	@echo "  make build         - Build the Spring Boot application (includes format check)."
	@echo "  make tests         - Run all tests."
	@echo "  make start         - Start the Spring Boot application (loads .env automatically)."
	@echo "  make setup         - Configure git hooks and generate Gradle wrapper."

format:
	@echo "Applying code formatting..."
	./gradlew spotlessApply

format-check:
	@echo "Checking code formatting..."
	./gradlew spotlessCheck

build:
	@echo "Building the application..."
	./gradlew clean build

tests:
	./gradlew clean test jacocoTestReport

start:
	@echo "Starting application (Spring Boot will load .env if present)..."
	./gradlew bootRun

setup:
	git config core.hooksPath .githooks
	gradle wrapper
