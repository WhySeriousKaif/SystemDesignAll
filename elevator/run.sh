#!/bin/bash

# Exit on error
set -e

echo "Building Elevator System Simulation..."

# Create output directory
mkdir -p out

# Find all Java source files and compile
find src -name "*.java" > sources.txt
javac -d out @sources.txt
rm sources.txt

echo "Build successful!"
echo "Running simulation..."
echo "==================================="

# Run the Main class
java -cp out com.elevator.Main
