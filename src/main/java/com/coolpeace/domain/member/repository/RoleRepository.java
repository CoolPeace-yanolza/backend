package com.coolpeace.domain.member.repository;

import com.coolpeace.domain.member.entity.Role;
import com.coolpeace.domain.member.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
