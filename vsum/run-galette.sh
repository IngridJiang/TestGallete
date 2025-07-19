#!/bin/bash

# Galette VSUM Runner Script
# This script runs the VSUM application with Galette instrumentation

# Configuration - Update these paths for your environment
INSTRUMENTED_JAVA_HOME="${INSTRUMENTED_JAVA_HOME}"

if [ -z "$INSTRUMENTED_JAVA_HOME" ]; then
    echo "Please set INSTRUMENTED_JAVA_HOME environment variable to your Galette-instrumented JDK path"
    echo "Example: export INSTRUMENTED_JAVA_HOME=/path/to/instrumented-jdk-17"
    echo ""
    echo "You can also set it in your ~/.bashrc or ~/.profile:"
    echo "echo 'export INSTRUMENTED_JAVA_HOME=/path/to/instrumented-jdk-17' >> ~/.bashrc"
    exit 1
fi

# Get script directory and project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Define paths relative to script location
AGENT_JAR="$PROJECT_ROOT/../galette-concolic-model-transformation/galette-agent/target/galette-agent-1.0.0-SNAPSHOT.jar"
CLASSES="$SCRIPT_DIR/target/classes"
CP_FILE="$SCRIPT_DIR/target/classpath.txt"

# Check if required files exist
if [ ! -f "$AGENT_JAR" ]; then
    echo "Agent JAR not found: $AGENT_JAR"
    echo "Please build Galette first: cd ../galette-concolic-model-transformation && mvn install"
    exit 1
fi

if [ ! -f "$CP_FILE" ]; then
    echo "Classpath file not found: $CP_FILE"
    echo "Please build the project first: mvn clean verify"
    exit 1
fi

# Read dependencies from classpath file
DEPS=$(cat "$CP_FILE")

# Combine classes and dependencies
FULL_CLASSPATH="$CLASSES:$DEPS"

echo "Running VSUM with Galette instrumentation..."
echo "Java: $INSTRUMENTED_JAVA_HOME/bin/java"
echo "Agent: $AGENT_JAR"
echo ""

# Run the application
"$INSTRUMENTED_JAVA_HOME/bin/java" \
  -cp "$FULL_CLASSPATH" \
  -Xbootclasspath/a:"$AGENT_JAR" \
  -javaagent:"$AGENT_JAR" \
  -Djava.compiler=NONE \
  tools.vitruv.methodologisttemplate.vsum.VSUMRunner