package com.coolpeace.domain.member.entity;

import com.coolpeace.domain.member.entity.type.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberRole> memberRoles = new ArrayList<>();

    private Role(RoleType roleType) {
        this.roleType = roleType;
    }

    public Role of(RoleType roleType) {
        return new Role(roleType);
    }
}
