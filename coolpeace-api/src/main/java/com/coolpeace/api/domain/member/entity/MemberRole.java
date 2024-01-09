package com.coolpeace.api.domain.member.entity;

import com.coolpeace.api.domain.member.entity.key.MemberRoleId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRole {
    @EmbeddedId
    private MemberRoleId memberRoleId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId("roleId")
    private Role role;

    private MemberRole(Member member, Role role) {
        this.memberRoleId = MemberRoleId.from(member.getId(), role.getId());
        this.member = member;
        this.role = role;
    }

    public static MemberRole from(Member member, Role role) {
        return new MemberRole(member, role);
    }
}
