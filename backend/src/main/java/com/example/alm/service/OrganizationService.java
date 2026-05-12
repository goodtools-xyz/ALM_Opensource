
package com.example.alm.service;

import com.example.alm.entity.Organization;
import com.example.alm.entity.Employee;
import com.example.alm.entity.Role;

import java.util.List;
import java.util.Map;

public interface OrganizationService {
    List<Organization> getAllOrganizations();
    Organization getOrganizationById(String orgId);
    List<Organization> getChildOrganizations(String parentOrgId);
    Organization createOrganization(Organization organization);
    Organization updateOrganization(String orgId, Organization organization);
    void deleteOrganization(String orgId);
    
    List<Employee> getAllEmployees();
    Employee getEmployeeById(String empId);
    List<Employee> getEmployeesByOrg(String orgId);
    Employee createEmployee(Employee employee);
    Employee updateEmployee(String empId, Employee employee);
    void deleteEmployee(String empId);
    
    List<Role> getAllRoles();
    Role getRoleById(String roleId);
    Role getRoleByCode(String code);
    Role createRole(Role role);
    Role updateRole(String roleId, Role role);
    void deleteRole(String roleId);
    
    Map<String, Object> syncFromExternal(String sourceType, Map<String, Object> data);
}
