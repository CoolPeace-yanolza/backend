package com.coolpeace.core.domain.member.repository;

import com.coolpeace.core.domain.member.entity.Role;
import com.coolpeace.core.domain.member.entity.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
