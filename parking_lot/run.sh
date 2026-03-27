#!/bin/bash

# 1. Create 'out' directory if it doesn't exist
mkdir -p out

# 2. Compile the whole project starting from Main
echo "🔧 Compiling Parking Lot System..."
javac -d out -sourcepath . parking_lot/java/main/Main.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo "🚀 Running the Parking Lot System..."
    echo -e "----------------------------------------\n"
    # 3. Run the compiled Main class
    java -cp out parking_lot.java.main.Main
else
    echo "❌ Compilation failed!"
    exit 1
fi
