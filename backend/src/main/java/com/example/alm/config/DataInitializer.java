package com.example.alm.config;

import com.example.alm.entity.*;
import com.example.alm.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            OrganizationRepository orgRepo,
            EmployeeRepository empRepo,
            RoleRepository roleRepo,
            HardwareProjectRepository hardwareProjectRepo,
            StructureProjectRepository structureProjectRepo,
            KnowledgeSpaceRepository knowledgeSpaceRepo,
            ApprovalFlowRepository approvalFlowRepo
    ) {
        return args -> {
            // Initialize Organizations
            Organization org1 = new Organization();
            org1.setOrgId("org-001");
            org1.setName("研发部");
            org1.setCode("RD");
            org1.setType("DEPARTMENT");
            org1.setStatus("ACTIVE");
            org1.setCreatedAt(LocalDateTime.now());
            orgRepo.save(org1);

            Organization org2 = new Organization();
            org2.setOrgId("org-002");
            org2.setName("产品部");
            org2.setCode("PD");
            org2.setType("DEPARTMENT");
            org2.setStatus("ACTIVE");
            org2.setCreatedAt(LocalDateTime.now());
            orgRepo.save(org2);

            // Initialize Employees
            Employee emp1 = new Employee();
            emp1.setEmpId("emp-001");
            emp1.setName("张三");
            emp1.setEmail("zhangsan@example.com");
            emp1.setJobTitle("高级工程师");
            emp1.setOrgId("org-001");
            emp1.setStatus("ACTIVE");
            emp1.setCreatedAt(LocalDateTime.now());
            empRepo.save(emp1);

            Employee emp2 = new Employee();
            emp2.setEmpId("emp-002");
            emp2.setName("李四");
            emp2.setEmail("lisi@example.com");
            emp2.setJobTitle("产品经理");
            emp2.setOrgId("org-002");
            emp2.setStatus("ACTIVE");
            emp2.setCreatedAt(LocalDateTime.now());
            empRepo.save(emp2);

            // Initialize Roles
            Role role1 = new Role();
            role1.setRoleId("role-001");
            role1.setName("管理员");
            role1.setCode("ADMIN");
            role1.setStatus("ACTIVE");
            role1.setDescription("系统管理员");
            role1.setPermissions("READ,WRITE,DELETE,ADMIN");
            role1.setCreatedAt(LocalDateTime.now());
            roleRepo.save(role1);

            Role role2 = new Role();
            role2.setRoleId("role-002");
            role2.setName("普通用户");
            role2.setCode("USER");
            role2.setStatus("ACTIVE");
            role2.setDescription("普通用户权限");
            role2.setPermissions("READ,WRITE");
            role2.setCreatedAt(LocalDateTime.now());
            roleRepo.save(role2);

            // Initialize Hardware Projects
            HardwareProject hwProject1 = new HardwareProject();
            hwProject1.setProjectId("hw-001");
            hwProject1.setName("智能家居控制器");
            hwProject1.setDescription("基于ARM Cortex-M4的智能家居控制中心");
            hwProject1.setStatus("IN_PROGRESS");
            hwProject1.setCreatedBy("emp-001");
            hwProject1.setCreatedAt(LocalDateTime.now());
            hardwareProjectRepo.save(hwProject1);

            HardwareProject hwProject2 = new HardwareProject();
            hwProject2.setProjectId("hw-002");
            hwProject2.setName("工业传感器模块");
            hwProject2.setDescription("高精度工业级传感器采集模块");
            hwProject2.setStatus("PLANNING");
            hwProject2.setCreatedBy("emp-001");
            hwProject2.setCreatedAt(LocalDateTime.now());
            hardwareProjectRepo.save(hwProject2);

            // Initialize Structure Projects
            StructureProject stProject1 = new StructureProject();
            stProject1.setProjectId("st-001");
            stProject1.setName("外壳结构设计");
            stProject1.setDescription("智能家居设备外壳结构设计");
            stProject1.setStatus("IN_PROGRESS");
            stProject1.setCreatedBy("emp-001");
            stProject1.setCreatedAt(LocalDateTime.now());
            structureProjectRepo.save(stProject1);

            // Initialize Knowledge Spaces
            KnowledgeSpace space1 = new KnowledgeSpace();
            space1.setSpaceId("space-001");
            space1.setName("技术文档");
            space1.setSpaceKey("TECH_DOCS");
            space1.setDescription("技术文档知识库");
            space1.setStatus("ACTIVE");
            space1.setCreatedBy("emp-001");
            space1.setCreatedAt(LocalDateTime.now());
            knowledgeSpaceRepo.save(space1);

            KnowledgeSpace space2 = new KnowledgeSpace();
            space2.setSpaceId("space-002");
            space2.setName("产品需求");
            space2.setSpaceKey("PRD");
            space2.setDescription("产品需求文档");
            space2.setStatus("ACTIVE");
            space2.setCreatedBy("emp-002");
            space2.setCreatedAt(LocalDateTime.now());
            knowledgeSpaceRepo.save(space2);

            // Initialize Approval Flows
            ApprovalFlow flow1 = new ApprovalFlow();
            flow1.setFlowId("flow-001");
            flow1.setName("项目立项审批");
            flow1.setType("PROJECT_APPROVAL");
            flow1.setDescription("项目立项审批流程");
            flow1.setStatus("ACTIVE");
            flow1.setCreatedAt(LocalDateTime.now());
            flow1.setCreatedBy("emp-001");
            approvalFlowRepo.save(flow1);

            System.out.println("========================================");
            System.out.println("Database initialized with sample data!");
            System.out.println("========================================");
            System.out.println("Organizations: 2");
            System.out.println("Employees: 2");
            System.out.println("Roles: 2");
            System.out.println("Hardware Projects: 2");
            System.out.println("Structure Projects: 1");
            System.out.println("Knowledge Spaces: 2");
            System.out.println("Approval Flows: 1");
            System.out.println("========================================");
        };
    }
}