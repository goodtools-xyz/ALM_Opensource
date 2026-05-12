
package com.example.alm.repository;

import com.example.alm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    List<Employee> findByOrgId(String orgId);
    List<Employee> findByStatus(String status);
    Employee findByEmail(String email);
    Employee findByExternalId(String externalId);
}
