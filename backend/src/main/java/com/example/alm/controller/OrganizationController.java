
package com.example.alm.controller;

import com.example.alm.entity.Organization;
import com.example.alm.entity.Employee;
import com.example.alm.entity.Role;
import com.example.alm.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/org")
public class OrganizationController {
    
    @Autowired
    private OrganizationService orgService;
    
    @GetMapping("/organizations")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(orgService.getAllOrganizations());
    }
    
    @GetMapping("/organizations/{orgId}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable String orgId) {
        Organization org = orgService.getOrganizationById(orgId);
        return org != null ? ResponseEntity.ok(org) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/organizations/{orgId}/children")
    public ResponseEntity<List<Organization>> getChildOrganizations(@PathVariable String orgId) {
        return ResponseEntity.ok(orgService.getChildOrganizations(orgId));
    }
    
    @PostMapping("/organizations")
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        return ResponseEntity.ok(orgService.createOrganization(organization));
    }
    
    @PutMapping("/organizations/{orgId}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable String orgId, @RequestBody Organization organization) {
        Organization updated = orgService.updateOrganization(orgId, organization);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/organizations/{orgId}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String orgId) {
        orgService.deleteOrganization(orgId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(orgService.getAllEmployees());
    }
    
    @GetMapping("/employees/{empId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String empId) {
        Employee emp = orgService.getEmployeeById(empId);
        return emp != null ? ResponseEntity.ok(emp) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/organizations/{orgId}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByOrg(@PathVariable String orgId) {
        return ResponseEntity.ok(orgService.getEmployeesByOrg(orgId));
    }
    
    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(orgService.createEmployee(employee));
    }
    
    @PutMapping("/employees/{empId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String empId, @RequestBody Employee employee) {
        Employee updated = orgService.updateEmployee(empId, employee);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/employees/{empId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String empId) {
        orgService.deleteEmployee(empId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(orgService.getAllRoles());
    }
    
    @GetMapping("/roles/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable String roleId) {
        Role role = orgService.getRoleById(roleId);
        return role != null ? ResponseEntity.ok(role) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/roles/code/{code}")
    public ResponseEntity<Role> getRoleByCode(@PathVariable String code) {
        Role role = orgService.getRoleByCode(code);
        return role != null ? ResponseEntity.ok(role) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(orgService.createRole(role));
    }
    
    @PutMapping("/roles/{roleId}")
    public ResponseEntity<Role> updateRole(@PathVariable String roleId, @RequestBody Role role) {
        Role updated = orgService.updateRole(roleId, role);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable String roleId) {
        orgService.deleteRole(roleId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/sync/{sourceType}")
    public ResponseEntity<Map<String, Object>> syncFromExternal(@PathVariable String sourceType, @RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(orgService.syncFromExternal(sourceType, data));
    }
}
