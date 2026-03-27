package com.scaler.concept.structural_patterns;

import java.util.ArrayList;
import java.util.List;

// 3. Composite Pattern
// Purpose: Handle tree-like structures (object inside object).

interface FileSystem {
    void ls();
}

class File implements FileSystem {
    private String name;
    
    public File(String name) { this.name = name; }
    
    @Override
    public void ls() {
        System.out.println("  📄 [File]: " + name);
    }
}

class Directory implements FileSystem {
    private String name;
    private List<FileSystem> children = new ArrayList<>();
    
    public Directory(String name) { this.name = name; }
    
    @Override
    public void ls() {
        System.out.println("📂 [Directory]: " + name);
        for (FileSystem child: children) {
            child.ls(); // Recursion
        }
    }
    
    public void add(FileSystem fs) {
        children.add(fs);
    }
}

public class CompositePattern {
    public static void main(String[] args) {
        System.out.println("--- Composite Pattern (File System) ---");
        Directory parent = new Directory("Root");
        parent.add(new File("README.md"));
        
        Directory src = new Directory("src");
        src.add(new File("Main.java"));
        src.add(new File("Utils.java"));
        
        parent.add(src);
        parent.ls();
    }
}
