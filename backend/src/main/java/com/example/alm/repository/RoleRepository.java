
package com.example.alm.repository;

import com.example.alm.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByCode(String code);
    List<Role> findByStatus(String status);
}
