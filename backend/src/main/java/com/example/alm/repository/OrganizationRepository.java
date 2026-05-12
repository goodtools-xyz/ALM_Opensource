
package com.example.alm.repository;

import com.example.alm.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {
    List<Organization> findByParentOrgId(String parentOrgId);
    List<Organization> findByType(String type);
    List<Organization> findByStatus(String status);
    Organization findByExternalId(String externalId);
}
