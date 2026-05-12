
package com.example.alm.service.impl;

import com.example.alm.entity.Organization;
import com.example.alm.entity.Employee;
import com.example.alm.entity.Role;
import com.example.alm.repository.OrganizationRepository;
import com.example.alm.repository.EmployeeRepository;
import com.example.alm.repository.RoleRepository;
import com.example.alm.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    
    @Autowired
    private OrganizationRepository orgRepository;
    
    @Autowired
    private EmployeeRepository empRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public List<Organization> getAllOrganizations() {
        return orgRepository.findAll();
    }
    
    @Override
    public Organization getOrganizationById(String orgId) {
        return orgRepository.findById(orgId).orElse(null);
    }
    
    @Override
    public List<Organization> getChildOrganizations(String parentOrgId) {
        return orgRepository.findByParentOrgId(parentOrgId);
    }
    
    @Override
    public Organization createOrganization(Organization organization) {
        organization.setOrgId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        organization.setCreatedAt(LocalDateTime.now());
        organization.setUpdatedAt(LocalDateTime.now());
        organization.setStatus("ACTIVE");
        return orgRepository.save(organization);
    }
    
    @Override
    public Organization updateOrganization(String orgId, Organization organization) {
        Organization existing = orgRepository.findById(orgId).orElse(null);
        if (existing != null) {
            existing.setName(organization.getName());
            existing.setCode(organization.getCode());
            existing.setType(organization.getType());
            existing.setStatus(organization.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            return orgRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteOrganization(String orgId) {
        orgRepository.deleteById(orgId);
    }
    
    @Override
    public List<Employee> getAllEmployees() {
        return empRepository.findAll();
    }
    
    @Override
    public Employee getEmployeeById(String empId) {
        return empRepository.findById(empId).orElse(null);
    }
    
    @Override
    public List<Employee> getEmployeesByOrg(String orgId) {
        return empRepository.findByOrgId(orgId);
    }
    
    @Override
    public Employee createEmployee(Employee employee) {
        employee.setEmpId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setStatus("ACTIVE");
        return empRepository.save(employee);
    }
    
    @Override
    public Employee updateEmployee(String empId, Employee employee) {
        Employee existing = empRepository.findById(empId).orElse(null);
        if (existing != null) {
            existing.setName(employee.getName());
            existing.setEmail(employee.getEmail());
            existing.setPhone(employee.getPhone());
            existing.setJobTitle(employee.getJobTitle());
            existing.setOrgId(employee.getOrgId());
            existing.setStatus(employee.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            return empRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteEmployee(String empId) {
        empRepository.deleteById(empId);
    }
    
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    
    @Override
    public Role getRoleById(String roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }
    
    @Override
    public Role getRoleByCode(String code) {
        return roleRepository.findByCode(code);
    }
    
    @Override
    public Role createRole(Role role) {
        role.setRoleId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        role.setCreatedAt(LocalDateTime.now());
        role.setStatus("ACTIVE");
        return roleRepository.save(role);
    }
    
    @Override
    public Role updateRole(String roleId, Role role) {
        Role existing = roleRepository.findById(roleId).orElse(null);
        if (existing != null) {
            existing.setName(role.getName());
            existing.setCode(role.getCode());
            existing.setDescription(role.getDescription());
            existing.setPermissions(role.getPermissions());
            existing.setStatus(role.getStatus());
            return roleRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
    }
    
    @Override
    public Map<String, Object> syncFromExternal(String sourceType, Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        int orgCount = 0;
        int empCount = 0;
        
        List<Map<String, Object>> orgs = (List<Map<String, Object>>) data.get("organizations");
        if (orgs != null) {
            for (Map<String, Object> orgData : orgs) {
                String externalId = (String) orgData.get("externalId");
                Organization existing = orgRepository.findByExternalId(externalId);
                
                if (existing != null) {
                    existing.setName((String) orgData.get("name"));
                    existing.setCode((String) orgData.get("code"));
                    existing.setSyncedAt(LocalDateTime.now());
                    orgRepository.save(existing);
                } else {
                    Organization org = new Organization();
                    org.setOrgId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
                    org.setName((String) orgData.get("name"));
                    org.setCode((String) orgData.get("code"));
                    org.setType((String) orgData.get("type"));
                    org.setParentOrgId((String) orgData.get("parentOrgId"));
                    org.setSyncSource(sourceType);
                    org.setExternalId(externalId);
                    org.setStatus("ACTIVE");
                    org.setCreatedAt(LocalDateTime.now());
                    org.setSyncedAt(LocalDateTime.now());
                    orgRepository.save(org);
                }
                orgCount++;
            }
        }
        
        List<Map<String, Object>> emps = (List<Map<String, Object>>) data.get("employees");
        if (emps != null) {
            for (Map<String, Object> empData : emps) {
                String externalId = (String) empData.get("externalId");
                Employee existing = empRepository.findByExternalId(externalId);
                
                if (existing != null) {
                    existing.setName((String) empData.get("name"));
                    existing.setEmail((String) empData.get("email"));
                    existing.setPhone((String) empData.get("phone"));
                    existing.setJobTitle((String) empData.get("jobTitle"));
                    existing.setOrgId((String) empData.get("orgId"));
                    existing.setSyncedAt(LocalDateTime.now());
                    empRepository.save(existing);
                } else {
                    Employee emp = new Employee();
                    emp.setEmpId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
                    emp.setName((String) empData.get("name"));
                    emp.setEmail((String) empData.get("email"));
                    emp.setPhone((String) empData.get("phone"));
                    emp.setJobTitle((String) empData.get("jobTitle"));
                    emp.setOrgId((String) empData.get("orgId"));
                    emp.setSyncSource(sourceType);
                    emp.setExternalId(externalId);
                    emp.setStatus("ACTIVE");
                    emp.setCreatedAt(LocalDateTime.now());
                    emp.setSyncedAt(LocalDateTime.now());
                    empRepository.save(emp);
                }
                empCount++;
            }
        }
        
        result.put("success", true);
        result.put("orgSynced", orgCount);
        result.put("empSynced", empCount);
        return result;
    }
}
