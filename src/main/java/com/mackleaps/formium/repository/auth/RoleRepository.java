package com.mackleaps.formium.repository.auth;

import com.mackleaps.formium.model.auth.Role;
import com.mackleaps.formium.model.auth.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleType(RoleType roleType);
    boolean existsByRoleType(RoleType roleType);
}
