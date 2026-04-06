# Distributed Cache System Design

## 🏗️ Complete UML Architectural Design
The following diagram provides a comprehensive view of the Distributed Cache architecture, showing the interaction between the Client, Load Balancer, Cache Nodes, and Persistent Storage.

```mermaid
classDiagram
    %% ======================
    %% CLIENT & ENTRY LAYER
    %% ======================
    class Client {
        +get(key)
        +put(key, value)
    }

    class LoadBalancer {
        -DistributionStrategy strategy
        -List~CacheNode~ nodes
        +get(key)
        +put(key, value)
    }

    %% ======================
    %% DISTRIBUTION STRATEGY
    %% ======================
    class DistributionStrategy {
        <<interface>>
        +getNode(key)
    }

    class ModuloStrategy {
        +getNode(key)
    }

    class ConsistentHashing {
        -SortedMap~int, CacheNode~ ring
        +getNode(key)
        +addNode(node)
    }

    DistributionStrategy <|.. ModuloStrategy
    DistributionStrategy <|.. ConsistentHashing

    %% ======================
    %% CACHE NODE LAYER
    %% ======================
    class CacheNode {
        -Cache localCache
        -DatabaseAdapter db
        +get(key)
        +put(key, value)
    }

    class Cache {
        <<interface>>
        +get(key)
        +put(key, value)
    }

    class LRUCache {
        -int capacity
        -Map~int, Node~ map
        -Node head, tail
        -ReentrantLock lock
        +get(key)
        +put(key, value)
    }

    class Node {
        -int key, value
        -Node prev, next
    }

    Cache <|.. LRUCache
    LRUCache --> Node : Doubly Linked List

    %% ======================
    %% PERSISTENCE LAYER
    %% ======================
    class DatabaseAdapter {
        <<interface>>
        +get(key)
    }

    class SQLDatabase { +get(key) }
    class NoSQLDatabase { +get(key) }

    DatabaseAdapter <|.. SQLDatabase
    DatabaseAdapter <|.. NoSQLDatabase

    %% ======================
    %% SYSTEM CONNECTIVITY
    %% ======================
    Client --> LoadBalancer : request
    LoadBalancer --> DistributionStrategy : hashing/routing
    LoadBalancer --> CacheNode : delegates storage
    CacheNode --> Cache : L1 Cache
    CacheNode --> DatabaseAdapter : L2 Persistence (Cache Miss)
```

---

## 🧩 Component Breakdown

### 1. Load Balancing & Distribution
- **DistributionStrategy**: Decouples the routing logic.
- **Consistent Hashing**: Ensures minimal data reshuffling when nodes are added or removed.

### 2. Cache Node (LRU)
- **Local Cache**: Each node runs an independent LRU cache.
- **Concurrency**: `ReentrantLock` ensures thread safety within the cache operations.

### 3. Persistence Layer
- **Write-Through/Read-Through**: Cache nodes interact with a database adapter to ensure data consistency and survive cache misses.