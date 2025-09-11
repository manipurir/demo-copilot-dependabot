#!/usr/bin/env bash
set -e

echo "🚀 Setting up Spring Boot Dev Environment (Gradle wrapper)..."

# Ensure Java is present
java -version

# Ensure wrapper is executable
if [ -f "./gradlew" ]; then
  chmod +x ./gradlew
  echo "Using project Gradle wrapper to warm up dependencies..."
  # Warm the cache; skip tests to save time
  ./gradlew --no-daemon build -x test || true
else
  echo "⚠️ gradlew not found. If you want a global Gradle, enable SDKMAN or install Gradle."
fi

echo "✅ DevContainer setup complete!"