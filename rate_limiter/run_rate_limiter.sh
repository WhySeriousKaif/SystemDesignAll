#!/bin/bash

# Exit on error
set -e

echo "🛡️  Building Rate Limiter System..."

# Create output directory
mkdir -p out

# Find all Java source files and compile
find src -name "*.java" > sources.txt
javac -d out @sources.txt
rm sources.txt

echo "✅ Build successful!"
echo "🚀 Running Rate Limiter Simulation..."
echo "======================================"

# Run the Main class
java -cp out com.ratelimiter.Main

echo -e "\n🚀 Running Proxy Example (ATM)..."
echo "======================================"
java -cp out com.ratelimiter.examples.ProxyExampleMain
