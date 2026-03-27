package com.scaler.concept.structural_patterns;

// 2. Proxy Pattern
// Purpose: Control access to original object (validation, logging, access control).

interface EmployeeDAO {
    void create(String employee);
}

class EmployeeDAOImpl implements EmployeeDAO {
    @Override
    public void create(String employee) {
        System.out.println("✅ [DAO] Creating employee: " + employee);
    }
}

class EmployeeDAOProxy implements EmployeeDAO {
    private EmployeeDAOImpl dao;
    private boolean isAdmin;
    
    public EmployeeDAOProxy(EmployeeDAOImpl dao, boolean isAdmin) {
        this.dao = dao;
        this.isAdmin = isAdmin;
    }
    
    @Override
    public void create(String employee) {
        if (isAdmin()) {
            System.out.println("🔑 [Proxy] Access granted for Admin.");
            dao.create(employee);
        } else {
            System.out.println("🛑 [Proxy] Access denied. Admin only.");
        }
    }
    
    private boolean isAdmin() {
        return isAdmin;
    }
}

public class ProxyPattern {
    public static void main(String[] args) {
        System.out.println("--- Proxy Pattern (Employee DAO) ---");
        EmployeeDAOImpl realService = new EmployeeDAOImpl();
        
        System.out.println("\n--- Testing Admin Access ---");
        EmployeeDAO adminProxy = new EmployeeDAOProxy(realService, true);
        adminProxy.create("John Doe");
        
        System.out.println("\n--- Testing Non-Admin Access ---");
        EmployeeDAO userProxy = new EmployeeDAOProxy(realService, false);
        userProxy.create("Jane Smith");
    }
}
