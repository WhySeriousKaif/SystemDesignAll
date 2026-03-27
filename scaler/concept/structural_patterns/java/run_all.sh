#!/bin/bash

# Exit on error
set -e

echo "🏗️  Building Structural Design Pattern Examples..."

# Create output directory
mkdir -p out

# Find all Java source files and compile
find . -name "*.java" > sources.txt
javac -d out @sources.txt
rm sources.txt

echo "✅ Build successful!"
echo "🚀 Running Structural Design Pattern Examples..."
echo "=============================================="

# Define the package prefix
PACKAGE="com.scaler.concept.structural_patterns"

# List of patterns to run
PATTERNS=(
    "DecoratorPattern"
    "ProxyPattern"
    "CompositePattern"
    "AdapterPattern"
    "BridgePattern"
    "FacadePattern"
    "FlyweightPattern"
)

# Run each pattern
for pattern in "${PATTERNS[@]}"; do
    echo -e "\n🔹 Executing: $pattern"
    echo "----------------------------------------------"
    java -cp out "$PACKAGE.$pattern"
done

echo -e "\n=============================================="
echo "✅ All Structural Design Patterns Executed."
